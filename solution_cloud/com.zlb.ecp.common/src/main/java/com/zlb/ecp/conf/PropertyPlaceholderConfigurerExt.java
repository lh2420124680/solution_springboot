package com.zlb.ecp.conf;

import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Description: 容器启动时读取所有property配置文件
 * @author Jane.Luo
 * @date 2017年10月12日 下午2:59:17 
 * @version V1.0
 */
public class PropertyPlaceholderConfigurerExt extends PropertyPlaceholderConfigurer {

	private static final Logger log = LoggerFactory.getLogger(PropertyPlaceholderConfigurerExt.class);

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			if ((key.toLowerCase().contains("jdbc.url")) || (key.toLowerCase().contains("jdbc.username"))
					|| (key.toLowerCase().contains("jdbc.password"))) {
				String val = (String) entry.getValue();
				log.info(">>>>开发模式<<<<数据库连接配置:key: " + key + ", value : " + val);
			}
		}
		super.processProperties(beanFactory, props);
	}
}
