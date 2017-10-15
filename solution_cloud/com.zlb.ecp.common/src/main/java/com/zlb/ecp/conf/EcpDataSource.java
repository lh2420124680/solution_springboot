package com.zlb.ecp.conf;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 用于方法是属于读写方式的注解和动态数据源切换的注解 
 * @author Jane.Luo
 * @date 2017年10月12日 下午2:58:02 
 * @version V1.0
 */
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EcpDataSource {
	String value() default "default_dataSource";

	boolean cascading() default true;

	boolean invokeWfAction() default false;
}
