package com.zlb.ecp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.zlb.ecp.conf.AppConfig;
import com.zlb.ecp.intercept.CommonInit;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@Import({ CommonInit.class,AppConfig.class })
@ImportResource("classpath:spring-login-consumer.xml")
@ComponentScan(basePackages = "com.zlb.ecp.**.service")
public class LoginConsumer {
	public static void main(String[] args) {
		SpringApplication.run(LoginConsumer.class, args);
	}
}
