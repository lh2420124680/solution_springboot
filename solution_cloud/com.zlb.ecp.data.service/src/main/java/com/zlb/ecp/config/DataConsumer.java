package com.zlb.ecp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.zlb.ecp.conf.CachingConfig;
import com.zlb.ecp.intercept.CommonInit;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@Import({ CommonInit.class, CachingConfig.class })
@ImportResource("classpath:spring-data-consumer.xml")
@ComponentScan(basePackages = {"com.zlb.ecp.data.service","com.zlb.ecp.data.biz","com.zlb.ecp.data.dao","com.zlb.ecp.data.api.service"})
public class DataConsumer {
	public static void main(String[] args) {
		SpringApplication.run(DataConsumer.class, args);
	}
}
