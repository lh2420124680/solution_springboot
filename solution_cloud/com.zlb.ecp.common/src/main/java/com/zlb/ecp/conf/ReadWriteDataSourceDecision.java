package com.zlb.ecp.conf;

/**
 * @Description: 读写数据源选择
 * @author Jane.Luo
 * @date 2017年10月12日 下午3:00:08 
 * @version V1.0
 */
public class ReadWriteDataSourceDecision {
	private static final String PREFIXED_DS = "default";

	public static enum DataSourceType {
		writeDBKey, readDBKey;

		private DataSourceType() {
		}
	}

	private static final ThreadLocal<String> holder = new ThreadLocal();

	public static void markWrite(String prefixed) {
		String prefixedDs = prefixed == null ? "default" : prefixed;
		holder.set(prefixedDs + "_" + DataSourceType.writeDBKey);
	}

	public static void markRead(String prefixed) {
		String prefixedDs = prefixed == null ? "default" : prefixed;
		holder.set(prefixedDs + "_" + DataSourceType.readDBKey);
	}

	public static void reset() {
		holder.set(null);
	}

	public static boolean isChoiceNone() {
		return null == holder.get();
	}

	public static boolean isChoiceWrite() {
		return holder.get() == null ? true : ((String) holder.get()).endsWith(DataSourceType.writeDBKey.name());
	}

	public static boolean isChoiceRead() {
		return holder.get() == null ? false : ((String) holder.get()).endsWith(DataSourceType.readDBKey.name());
	}

	public static String getDsKey() {
		return holder.get() != null ? (String) holder.get() : null;
	}
}
