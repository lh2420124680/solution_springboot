package com.zlb.ecp.helper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);
	private ObjectMapper mapper;
	
	public static String object2json(Object obj) { 
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (((obj instanceof String)) || ((obj instanceof Integer)) || ((obj instanceof Float))
				|| ((obj instanceof Boolean)) || ((obj instanceof Short)) || ((obj instanceof Double))
				|| ((obj instanceof Long)) || ((obj instanceof BigDecimal)) || ((obj instanceof BigInteger))
				|| ((obj instanceof Byte))) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if ((obj instanceof Object[])) {
			json.append(array2json((Object[]) obj));
		} else if ((obj instanceof List)) {
			json.append(list2json((List) obj));
		} else if ((obj instanceof Map)) {
			json.append(map2json((Map) obj));
		} else if ((obj instanceof Set)) {
			json.append(set2json((Set) obj));
		} else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (props != null) {
			for (PropertyDescriptor prop : props) {
				try {
					String name = object2json(prop.getName());
					String value = object2json(prop.getReadMethod().invoke(bean, new Object[0]));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((list != null) && (list.size() > 0)) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((array != null) && (array.length > 0)) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if ((map != null) && (map.size() > 0)) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if ((set != null) && (set.size() > 0)) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String string2json(String s) {
		if (null == s) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if ((ch >= 0) && (ch <= '\037')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
				break;
			}
		}
		return sb.toString();
	}

	public JsonHelper() {
		this(null);
	}

	public JsonHelper(JsonInclude.Include include) {
		this.mapper = new ObjectMapper();
		if (include != null) {
			this.mapper.setSerializationInclusion(include);

			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.mapper.setDateFormat(fmt);
		}
		this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public static JsonHelper nonEmptyMapper() {
		return new JsonHelper(JsonInclude.Include.NON_EMPTY);
	}

	public static JsonHelper alwaysMapper() {
		return new JsonHelper();
	}

	public static JsonHelper nonDefaultMapper() {
		return new JsonHelper(JsonInclude.Include.NON_DEFAULT);
	}

	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return this.mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
		}
		return null;
	}

	public String toJson(Object o) {
		try {
			return this.mapper.writeValueAsString(o);
		} catch (IOException e) {
			logger.error("parse json error:" + e.getMessage(), e);
		}
		return null;
	}
}
