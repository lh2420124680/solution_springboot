package com.zlb.ecp.intercept;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description: 拦截器配置类
 * @author Jane.Luo
 * @date 2017年10月12日 下午3:04:26 
 * @version V1.0
 */
@Configuration
public class CommonInit extends WebMvcConfigurerAdapter {
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则 excludePathPatterns 用户排除拦截
        registry.addInterceptor(new PtInterceptor()).addPathPatterns("/**/*.do").excludePathPatterns("/ecp/login/**");
        super.addInterceptors(registry);
    }
}
