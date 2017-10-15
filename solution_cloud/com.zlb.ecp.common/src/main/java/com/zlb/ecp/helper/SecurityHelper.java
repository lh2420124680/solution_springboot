package com.zlb.ecp.helper;

import com.sun.crypto.provider.SunJCE;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHelper {
	private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);
	private static String STARTFIX = "\016";
	private static String ENDFIX = "\017";
	private static int KEY = 238;
	private static final char[] CHARS = "0123456789ABCDEF".toCharArray();
	public static String key = "8ABC7DLO5MN6Z9EFGdeJfghijkHIVrstuvwWSTUXYabclmnopqKPQRxyz01234";
	public static String dbkey = "0715123456789088";
	static boolean flag = true;
	static int keyLen;
	static int keyLenLen;
	static int[] keyArray = new int[''];
	private static final String Algorithm = "DES";

	public static String MD5(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String result = "";

			byte[] temp = md5.digest(encryptStr.getBytes(Charset.forName("UTF-8")));
			for (int i = 0; i < temp.length; i++) {
				result = result + Integer.toHexString(0xFF & temp[i] | 0xFFFFFF00).substring(6);
			}
			return result.toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			logger.error("获取信息摘要实例错误", e);
		} catch (Exception e) {
			logger.error("计算MD5码失败", e);
		}
		return encryptStr;
	}

	public static String EncryptCharToSelf(String value) {
		try {
			if (value == null) {
				return null;
			}
			StringBuilder builder = new StringBuilder();

			char[] chars = charConvert(value);
			for (int i = 0; i < chars.length; i++) {
				byte[] bytes = String.valueOf(chars[i]).getBytes("UTF-16LE");

				builder.append(STARTFIX + Byte2String(ByteConvert(bytes)) + ENDFIX);
			}
			if (chars.length == 0) {
				builder.append(STARTFIX + ENDFIX);
			}
			return builder.toString();
		} catch (Exception ex) {
		}
		return value;
	}

	public static String DecryptCharFromSelf(String value) {
		try {
			if (value == null) {
				return null;
			}
			if (value == "STARTFIX + ENDFIX") {
				return "";
			}
			if (!IsEncrypt(value).booleanValue()) {
				return value;
			}
			value = value.replaceAll(STARTFIX + ENDFIX, "");

			Pattern p1 = Pattern.compile(STARTFIX + "[^" + ENDFIX + "]*" + ENDFIX);
			Matcher m1 = p1.matcher(value);
			while (m1.find()) {
				String strTmp = m1.group();
				String strNewTmp = StringConvert(ByteToString(ByteConvert(String2Byte(Revert(strTmp)))));

				value = value.replaceAll(strTmp, strNewTmp);
			}
			return value;
		} catch (Exception ex) {
		}
		return value;
	}

	private static String ByteToString(byte[] srcobj) {
		return ByteToString(srcobj, "UTF-16LE");
	}

	private static String ByteToString(byte[] srcObj, String charEncode) {
		String destObj = null;
		try {
			destObj = new String(srcObj, charEncode);
		} catch (Exception e) {
			logger.error("转换错误：" + e.getMessage());
		}
		return destObj.replaceAll("", " ");
	}

	public static String Revert(String obj) {
		if (obj == null) {
			return obj;
		}
		if (obj.length() < STARTFIX.length() + ENDFIX.length()) {
			return obj;
		}
		return obj.substring(STARTFIX.length(), obj.length() - ENDFIX.length());
	}

	public static byte[] String2Byte(String Value) {
		if (Value.length() == 0) {
			return new byte[0];
		}
		byte[] bytes = new byte[Value.length() / 2];
		for (int x = 0; x < Value.length() / 2; x++) {
			bytes[x] = ((byte) Integer.parseInt(Value.substring(x * 2, x * 2 + 2), 16));
		}
		return bytes;
	}

	private static Boolean IsEncrypt(String Value) {
		if (Value == null) {
			return Boolean.valueOf(false);
		}
		if (Value.length() == 0) {
			return Boolean.valueOf(false);
		}
		if (Value == "STARTFIX + ENDFIX") {
			return Boolean.valueOf(true);
		}
		Pattern p1 = Pattern.compile(STARTFIX + "[^" + ENDFIX + "]*" + ENDFIX);
		Matcher m1 = p1.matcher(Value);
		if (m1.find()) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	private static String Byte2String(byte[] bytes) {
		if (bytes.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(CHARS[((b & 0xFF) >> 4)]);
			sb.append(CHARS[(b & 0xF)]);
		}
		return sb.toString();
	}

	private static byte[] ByteConvert(byte[] bytes) {
		if (bytes == null) {
			return bytes;
		}
		if (bytes.length == 0) {
			return bytes;
		}
		byte[] tys = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			tys[i] = bytes[(bytes.length - 1 - i)];
		}
		return tys;
	}

	private static char[] charConvert(String value) {
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = ((char) (chars[i] ^ KEY));
		}
		return chars;
	}

	public static String StringConvert(String Value) {
		char[] chars = Value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = ((char) (chars[i] ^ KEY));
		}
		return new String(chars);
	}

	static void init() {
		keyLen = key.length();
		keyLenLen = keyLen * keyLen;
		for (int x = 0; x < key.length(); x++) {
			int index = key.charAt(x);
			keyArray[index] = x;
		}
		flag = false;
	}

	public static String Encrypt(String str) {
		if (flag) {
			init();
		}
		int strLen = str.length();
		List<Character> t = new ArrayList(strLen * 3);
		for (int i = 0; i < strLen; i++) {
			int charVal = str.charAt(i);
			if (charVal < keyLenLen) {
				t.add(Character.valueOf(key.charAt(charVal / keyLen)));
				t.add(Character.valueOf(key.charAt(charVal % keyLen)));
			} else {
				t.add(Character.valueOf(key.charAt(charVal / keyLenLen + 5)));
				t.add(Character.valueOf(key.charAt(charVal / keyLen % keyLen)));
				t.add(Character.valueOf(key.charAt(charVal % keyLen)));
			}
		}
		char[] charArr = new char[t.size()];
		Object[] tArr = t.toArray();
		for (int i = 0; i < t.size(); i++) {
			charArr[i] = ((Character) tArr[i]).charValue();
		}
		String s = new String(charArr);
		return String.valueOf(s.length()).length() + String.valueOf(s.length()) + s;
	}

	public static String Decrypt(String str) {
		if ((str == null) || ("".equals(str))) {
			return "";
		}
		if (flag) {
			init();
		}
		int valueOfFirstChar = Integer.parseInt(String.valueOf(str.charAt(0)));
		int dataLen = Integer.parseInt(str.substring(1, 1 + valueOfFirstChar));

		int dataIndex = String.valueOf(dataLen).length() + 1;
		if (str.length() != dataLen + dataIndex) {
			return "";
		}
		int strLen = str.length();

		List<Character> t = new ArrayList(strLen * 3);
		for (; dataIndex < strLen; dataIndex++) {
			int charValue = keyArray[str.charAt(dataIndex)];
			dataIndex++;
			if (charValue < 5) {
				dataLen = charValue * keyLen + keyArray[str.charAt(dataIndex)];
			} else {
				dataLen = (charValue - 5) * keyLenLen + keyArray[str.charAt(dataIndex)] * keyLen;
				dataIndex++;
				dataLen += keyArray[str.charAt(dataIndex)];
			}
			t.add(Character.valueOf((char) dataLen));
		}
		char[] charArr = new char[t.size()];
		Object[] tArr = t.toArray();
		for (int i = 0; i < t.size(); i++) {
			charArr[i] = ((Character) tArr[i]).charValue();
		}
		String s = new String(charArr);
		return s;
	}

	public static String DBEncrypt(String str) {
		byte[] bytekey = hex2byte(dbkey);
		Security.addProvider(new SunJCE());
		byte[] encrypt = encryptMode(bytekey, str.getBytes());
		return byte2hex(encrypt);
	}

	public static String DBDecrypt(String str) {
		byte[] bytekey = hex2byte(dbkey);
		Security.addProvider(new SunJCE());
		byte[] decrypt = decryptMode(bytekey, hex2byte(str));
		return new String(decrypt);
	}

	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");

			Cipher c1 = Cipher.getInstance("DES");
			c1.init(1, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");

			Cipher c1 = Cipher.getInstance("DES");
			c1.init(2, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1) {
				hs = hs + "";
			}
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		int i = 0;
		int j = 0;
		for (int l = hex.length(); i < l; j++) {
			String swap = "" + arr[(i++)] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
			i++;
		}
		return b;
	}
}
