package com.zlb.ecp.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Description: 单独服务启动配置类 
 * @author Jane.Luo
 * @date 2017年10月12日 下午2:57:07 
 * @version V1.0
 */
@Configuration
@ImportResource({ "classpath:com-zlb-mg.xml" })
public class CachingConfig { }
