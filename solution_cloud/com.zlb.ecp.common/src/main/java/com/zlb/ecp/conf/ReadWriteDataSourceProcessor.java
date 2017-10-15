package com.zlb.ecp.conf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @Description: 读写数据源或切换数据源的类，此类在配置文件中是切面 
 * @author Jane.Luo
 * @date 2017年10月12日 下午3:00:24 
 * @version V1.0
 */
public class ReadWriteDataSourceProcessor implements BeanPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(ReadWriteDataSourceProcessor.class);
	private boolean forceChoiceReadWhenWrite = false;
	private Map<String, Boolean> readMethodMap = new HashMap();
	private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal();
	private static Map<String, Boolean> methodIsReadCache = new ConcurrentHashMap();

	public static void clearProcessorDataSource() {
		dataSourceHolder.set(null);
	}

	public static void setProcessorDataSource(String dsKey) {
		dataSourceHolder.set(dsKey);
	}

	public void setForceChoiceReadWhenWrite(boolean forceChoiceReadWhenWrite) {
		this.forceChoiceReadWhenWrite = forceChoiceReadWhenWrite;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!(bean instanceof NameMatchTransactionAttributeSource)) {
			return bean;
		}
		try {
			NameMatchTransactionAttributeSource transactionAttributeSource = (NameMatchTransactionAttributeSource) bean;
			Field nameMapField = ReflectionUtils.findField(NameMatchTransactionAttributeSource.class, "nameMap");
			nameMapField.setAccessible(true);

			Map<String, TransactionAttribute> nameMap = (Map) nameMapField.get(transactionAttributeSource);
			for (Map.Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
				RuleBasedTransactionAttribute attr = (RuleBasedTransactionAttribute) entry.getValue();
				if (attr.isReadOnly()) {
					String methodName = (String) entry.getKey();
					Boolean isForceChoiceRead = Boolean.FALSE;
					if (this.forceChoiceReadWhenWrite) {
						attr.setPropagationBehavior(Propagation.NOT_SUPPORTED.value());
						isForceChoiceRead = Boolean.TRUE;
					} else {
						attr.setPropagationBehavior(Propagation.SUPPORTS.value());
					}
					log.info("read/write transaction process  method:{} force read:{}", methodName, isForceChoiceRead);
					this.readMethodMap.put(methodName, isForceChoiceRead);
				}
			}
		} catch (Exception e) {
			throw new ReadWriteDataSourceTransactionException("process read/write transaction error", e);
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	private class ReadWriteDataSourceTransactionException extends NestedRuntimeException {
		private static final long serialVersionUID = -7831040288687947725L;

		public ReadWriteDataSourceTransactionException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public Object determineReadOrWriteDB(ProceedingJoinPoint pjp) throws Throwable {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Object target = pjp.getTarget();
		String cacheKey = target.getClass().getName() + "." + method.getName();
		Boolean isReadCacheValue = (Boolean) methodIsReadCache.get(cacheKey);
		if (isReadCacheValue == null) {
			isReadCacheValue = Boolean.valueOf(isChoiceReadDB(method));
			methodIsReadCache.put(cacheKey, isReadCacheValue);
			log.info("缓存方法数为{},读写方式{}", Integer.valueOf(methodIsReadCache.size()), isReadCacheValue);
		}
		String dsPrefixed = getDataSourceIdPrefixed(method);
		if (isReadCacheValue.booleanValue()) {
			ReadWriteDataSourceDecision.markRead(dsPrefixed);
		} else {
			ReadWriteDataSourceDecision.markWrite(dsPrefixed);
		}
		try {
			return pjp.proceed();
		} finally {
			ReadWriteDataSourceDecision.reset();
		}
	}

	private String getDataSourceIdPrefixed(Method method) {
		EcpDataSource dataSourceAnno = (EcpDataSource) AnnotationUtils.findAnnotation(method, EcpDataSource.class);
		if ((dataSourceAnno != null) && (dataSourceAnno.cascading())) {
			dataSourceHolder.set(dataSourceAnno.value());
		} else if ((dataSourceAnno != null) && (!dataSourceAnno.cascading())) {
			dataSourceHolder.set(null);
		}
		if (dataSourceHolder.get() != null) {
			return (String) dataSourceHolder.get();
		}
		return dataSourceAnno == null ? null : dataSourceAnno.value();
	}

	private boolean isChoiceReadDB(Method method) {
		Transactional transactionalAnno = (Transactional) AnnotationUtils.findAnnotation(method, Transactional.class);
		if (transactionalAnno == null) {
			return isChoiceReadDB(method.getName()).booleanValue();
		}
		if (transactionalAnno.readOnly()) {
			return true;
		}
		return false;
	}

	private Boolean isChoiceReadDB(String methodName) {
		String bestNameMatch = null;
		for (String mappedName : this.readMethodMap.keySet()) {
			if (isMatch(methodName, mappedName)) {
				bestNameMatch = mappedName;
				break;
			}
		}
		Boolean isForceChoiceRead = (Boolean) this.readMethodMap.get(bestNameMatch);
		if (isForceChoiceRead == Boolean.TRUE) {
			return Boolean.valueOf(true);
		}
		if (ReadWriteDataSourceDecision.isChoiceWrite()) {
			return Boolean.valueOf(false);
		}
		if (isForceChoiceRead != null) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}
}
