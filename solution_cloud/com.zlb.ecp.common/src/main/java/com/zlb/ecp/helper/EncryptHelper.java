package com.zlb.ecp.helper;

import java.security.*;
import javax.crypto.*;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储. 方法: void
 * getKey(String strKey)从strKey的字条生成一个Key String getEncString(String
 * strMing)对strMing进行加密,返回String密文 String getDesString(String
 * strMi)对strMin进行解密,返回String明文 byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[]
 * getDesCode(byte[] byteD)byte[]型的解密
 */
public class EncryptHelper {
	private Key key;

	// 根据参数生成KEY
	public void setKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(strKey.getBytes("UTF-8"));
			// _generator.init(new SecureRandom(strKey.getBytes()));
			_generator.init(random);
			this.key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 加密String明文输入,String密文输出
	public String getEncString(String strMing) {
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byte[] byteMing = strMing.getBytes("UTF8");
			byte[] byteMi = this.getEncCode(byteMing);
			return base64en.encode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 加密以byte[]明文输入,byte[]密文输出
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}

		return byteFina;
	}

	// 解密:以String密文输入,String明文输出
	public String getDesString(String strMi) {
		BASE64Decoder base64De = new BASE64Decoder();
		try {
			byte[] byteMi = base64De.decodeBuffer(strMi);
			byte[] byteMing = this.getDesCode(byteMi);
			return new String(byteMing, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解密以byte[]密文输入,以byte[]明文输出
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	// 设置密文
	public static String setIdCard(String key, String idCard) {
		if (!StringHelper.IsEmptyOrNull(idCard)) {
			if (idCard.length() > 20 || idCard.contains("***")) {
				return idCard;
			}
			EncryptHelper e = new EncryptHelper();
			e.setKey(key);
			return e.getEncString(idCard);
		}
		return "";
	}

	// 获取密文
	public static String getIdCard(String key, String idCard) {
		String card = "";
		if (!StringHelper.IsEmptyOrNull(idCard)) {
			if (idCard.length() < 20) {
				return idCard;
			}
			EncryptHelper e = new EncryptHelper();
			e.setKey(key);
			card = e.getDesString(idCard);
			card = card.substring(0, 4) + "************" + card.substring(card.length() - 2, card.length());
		}
		return card;
	}

	// 获取密文具体信息
	public static String getIdCardInfo(String key, String idCard) {
		String card = "";
		if (!StringHelper.IsEmptyOrNull(idCard)) {
			if (idCard.length() < 20) {
				return idCard;
			}
			EncryptHelper e = new EncryptHelper();
			e.setKey(key);
			card = e.getDesString(idCard);
		}
		return card;
	}

	public static void main(String[] args) {
		/*
		 * EncryptUtils e = new EncryptUtils(); EncryptUtils e1 = new
		 * EncryptUtils(); e.setKey("IDCARD");
		 */
		// e1.setKey("ww_ping@qq.com");
		// e.getEncString("ww64698990");
		System.out.println(setIdCard("IDCARD", "460006198805033324"));
		System.out.println(getIdCard("IDCARD", "WcEB5Kec/IiwILBsdnoTkosFDDhsNZps"));
		System.out.println(getIdCardInfo("IDCARD", "WcEB5Kec/IiwILBsdnoTkosFDDhsNZps"));

		String s = "E:\\AA/BB";

		String a = s.replaceAll("\\\\", "\\\\\\\\");
		System.out.println("" + a);
	}
}
