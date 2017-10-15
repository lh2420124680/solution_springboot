package com.zlb.ecp.conf;

import java.util.Map;

/**
 * @Description: 当前线程持有类
 * @author Jane.Luo
 * @date 2017年10月12日 下午3:02:57 
 * @version V1.0
 */
public class ContextLocalHold {
	
	private static final ThreadLocal<Map<String, String>> holder = new ThreadLocal();

	public static void setContext(Map<String, String> param) {
		holder.set(param);
	}

	public static Map<String, String> getContext() {
		return (Map) holder.get();
	}

	public static void clear() {
		holder.set(null);
	}
}
