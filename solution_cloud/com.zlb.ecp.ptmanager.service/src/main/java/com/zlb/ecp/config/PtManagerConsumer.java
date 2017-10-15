package com.zlb.ecp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.zlb.ecp.conf.AppConfig;
import com.zlb.ecp.intercept.CommonInit;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@Import({ CommonInit.class, AppConfig.class })
@ImportResource("classpath:spring-ptmanager-consumer.xml")
@ComponentScan(basePackages = {"com.zlb.ecp.ptmanager.**.service","com.zlb.ecp.ptmanager.**.biz","com.zlb.ecp.ptmanager.**.dao"})
public class PtManagerConsumer {
	public static void main(String[] args) {
		SpringApplication.run(PtManagerConsumer.class, args);
	}
}
