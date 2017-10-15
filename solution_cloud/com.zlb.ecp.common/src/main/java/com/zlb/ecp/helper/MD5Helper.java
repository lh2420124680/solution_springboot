package com.zlb.ecp.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class MD5Helper {
    //0的ASCII码
    private static final int ASCII_0=48;
    //9的ASCII码
    private static final int ASCII_9=57;
    //A的ASCII码
    private static final int ASCII_A=65;
    //F的ASCII码
    private static final int ASCII_F=70;
    //a的ASCII码
    private static final int ASCII_a=97;
    //f的ASCII码
    private static final int ASCII_f=102;
    
    
    //可表示16进制数字的字符
    private static final char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                                            'A', 'B', 'C', 'D', 'E', 'F' };
    //可表示16进制数字的字符
    private static final char psChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'Q', 'R', 'S', 'T', 'U', 'V' ,
                                           'A', 'B', 'C', 'D', 'E', 'F' ,'J', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'
                                           };
    
    private static final String HASH_MD5 = "MD5";
    
    
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	
	public static String byteArrayToHexString(final byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	
	private static String byteToHexString(final byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
	public static String MD5Encode(final String origin) {
		String resultString = null;
	
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			//resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			//resultString = Base64.encodeBase64String(md.digest(resultString.getBytes("UTF-8")));
			resultString = null;//Base64.encodeBase64String(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception ex) {
	
		}
		return resultString;
	}
	
	public static String MD5ToHex(final String origin) {
		String resultString = null;
		
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			
		}
		return resultString;
	}

    /**
     * 获取字节数组MD5码
     * @param bs
     * @return
     */
    public final static String encoding(byte[] bs) {
        
        String encodingStr = null;
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(HASH_MD5);
            mdTemp.update(bs);
            
            return toHexString( mdTemp.digest() );
            
        } catch (Exception e) {
        }

        return encodingStr;
    }
    
    /**
     * 获取字符串MD5码
     * @param text
     * @return
     */
    public final static String encoding(String text) {
        if( text==null ){
            return null;
        }
        return encoding( text.getBytes() );
    }
    
    
    public final static String encodeTwice(String text) {
        if( text==null ){
            return null;
        }
        String md5Once = encoding( text.getBytes() );
        return encoding(md5Once.getBytes());
    }
    
    /**
     * 获取文件内容MD5码
     * @param filePath
     * @return
     */
    public final static String encodingFile(String filePath){
         InputStream fis=null;
         try{
             fis = new FileInputStream(filePath);
             return encoding(fis);
         }catch( Exception ee){
             return null;
         }
         finally{
             if(fis!=null ){
                 try{ fis.close(); }catch( Exception ex){}
             }
         }
    }
    
    /**
     * 获取输入流MD5码
     * @param fis
     * @return
     * @throws Exception
     */
    public final static String encoding(InputStream fis) throws Exception{
         byte[] buffer = new byte[1024];  
         MessageDigest md5 = MessageDigest.getInstance(HASH_MD5);  
         int numRead = 0;  
         while ((numRead = fis.read(buffer)) > 0) {  
             md5.update(buffer, 0, numRead);  
         }
         return toHexString(md5.digest());
    }
    
    /**
     * 转换为用16进制字符表示的MD5
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {  
         StringBuilder sb = new StringBuilder(b.length * 2);  
         for (int i = 0; i < b.length; i++) {  
             sb.append(hexChar[(b[i] & 0xf0) >>> 4]);  
             sb.append(hexChar[b[i] & 0x0f]);  
         }  
         return sb.toString();  
    }
    
    /**
     * 检验是否是合法的MD5串
     * @param md5Str
     * @return
     */
    public static boolean validate(String md5Str){
        if(md5Str==null || md5Str.length()!=32 ){
            return false;
        }
        byte[] by = md5Str.getBytes();
        for( int i=0;i<by.length;i++ ){
            int asciiValue = (int)by[i];
            if(    asciiValue<ASCII_0
                    || ( asciiValue>ASCII_9 && asciiValue<ASCII_A)
                    || ( asciiValue>ASCII_F && asciiValue<ASCII_a)
                    || asciiValue>ASCII_f ){
                return false;
            }
        }
        return true;
    }
    
    /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5"); 
            byte[] byteArray = inStr.getBytes("utf-8");
            byte[] md5Bytes = md5.digest(byteArray);  
            StringBuffer hexValue = new StringBuffer();  
            for (int i = 0; i < md5Bytes.length; i++){  
                int val = ((int) md5Bytes[i]) & 0xff;  
                if (val < 16)  
                    hexValue.append("0");  
                hexValue.append(Integer.toHexString(val));  
            }  
            return hexValue.toString(); 
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        /*char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i]; */ 
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
    	if(!StringHelper.IsEmptyOrNull(inStr)){
    		if(inStr.length()==32){
    			return inStr;
    		}
	        else{
	        	return string2MD5(inStr);
	        }
    	}
    	return inStr;
       /* if(StringUtils.isNotEmptyOrNull(inStr) && inStr.length()==32){
        	return inStr;
        }
        char[] a = new char[32];
        char[] strChar= inStr.toCharArray(); 
        
        //if(strChar.length<32){
        	for(int j=0;j<32;j++){
        		if(j<strChar.length){
        			a[j]=strChar[j];
        		}else{
        			a[j] = 'k';
        		}
        	}
        //}
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 'y');  
        }  
        String s = new String(a);  
        return s;  */
  
    }  
  
    public static String sha1(String data) {
    	MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.update(data.getBytes());
			StringBuffer buf = new StringBuffer();
			byte[] bits = md.digest();
			for(int i=0;i<bits.length;i++){
				int a = bits[i];
				if(a<0) a+=256;
				if(a<16) buf.append("0");
				buf.append(Integer.toHexString(a));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    
    // 测试主函数  
    public static void main(String args[]) {  
        String s = new String("http://127.0.0.1:8086/zlbapp/ptmanager");  
        System.out.println("原始：" + s);  
        System.out.println("MD5后：" + string2MD5(s));  
        System.out.println("加密的：" + convertMD5(s));  
        System.out.println("解密的：" + convertMD5(convertMD5(s)));  
  
    }  
}
