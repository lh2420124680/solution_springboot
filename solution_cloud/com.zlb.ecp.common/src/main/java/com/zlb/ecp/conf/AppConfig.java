package com.zlb.ecp.conf;

import org.menagerie.DefaultZkSessionManager;
import org.menagerie.ZkSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Description: 容器启动入口配置类 
 * @author Jane.Luo
 * @date 2017年10月12日 下午2:56:16 
 * @version V1.0
 */
@Configuration
@ImportResource({ "classpath:applicationContext.xml" })
public class AppConfig {
/*
	@Value("${zookeeper.address:localhost:2181}")
	private String hostString;
	private int timeout = 2000;

	@Bean
	public ZkSessionManager zkSessionManager() {
		ZkSessionManager zkSessionManager = new DefaultZkSessionManager(this.hostString, this.timeout);
		return zkSessionManager;
	}*/
}
