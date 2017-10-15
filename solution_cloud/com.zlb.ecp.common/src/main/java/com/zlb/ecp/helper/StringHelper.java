package com.zlb.ecp.helper;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringHelper {

	private static final Logger logger = LoggerFactory.getLogger(StringHelper.class);
	public static final String EMPTY = "";

	public static Boolean IsEmptyOrNull(Object obj) {
		if (obj == null) {
			return Boolean.valueOf(true);
		}
		if ("".equals(obj)) {
			return Boolean.valueOf(true);
		}
		if ((obj instanceof String)) {
			if (((String) obj).length() == 0) {
				return Boolean.valueOf(true);
			}
		} else if ((obj instanceof Collection)) {
			if (((Collection) obj).size() == 0) {
				return Boolean.valueOf(true);
			}
		} else if (((obj instanceof Map)) && (((Map) obj).size() == 0)) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	public static String TranString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	public static String TranString(Object obj, String defaultval) {
		if (obj == null) {
			return defaultval;
		}
		return obj.toString();
	}

	public static String TranSeachString(Object obj) {
		if (obj == null) {
			return "";
		}
		String result = obj.toString();
		if ((result.startsWith("%")) || (result.startsWith("_"))) {
			result = "\\" + result;
		}
		if ((!"\\%".equals(result)) && (result.length() >= 2) && ((result.endsWith("%")) || (result.endsWith("_")))) {
			String tmp1 = result.substring(result.length() - 1, result.length());
			String tmp2 = result.substring(0, result.length() - 1);
			result = tmp2 + "\\" + tmp1;
		}
		return result;
	}

	public static String ByteToString(byte[] srcobj) {
		return ByteToString(srcobj, "UTF-16LE");
	}

	public static String ByteToString(byte[] srcObj, String charEncode) {
		String destObj = null;
		try {
			destObj = new String(srcObj, charEncode);
		} catch (Exception e) {
			logger.error("转换错误：" + e.getMessage());
		}
		return destObj.replaceAll("", " ");
	}

	public static String GetGUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
