package com.zlb.ecp.config;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.zlb.ecp.conf.AppConfig;

@Configuration
@ImportResource({ "classpath:spring-login-provider.xml" })
@Import(AppConfig.class)
public class LoginProvider {
	public static void main(String[] args) throws IOException {
		// 新建并设置启动环境
		SpringApplication app = new SpringApplication(LoginProvider.class);
		app.setWebEnvironment(false);
		// 运行
		app.run(args);
		com.alibaba.dubbo.container.Main.main(args);
	}
}
