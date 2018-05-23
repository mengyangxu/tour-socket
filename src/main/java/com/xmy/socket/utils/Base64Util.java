package com.xmy.socket.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

public class Base64Util {
	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		
		if(s!=null){
			return s.replaceAll("[\\s*\t\n\r]", "");
		}else{
			return null;
		}
		 
	}

	// 解密
	public static String getFromBase64(String s) {
		
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String bytes2Long(String temp) {  
	    byte[] byteNum = temp.getBytes();
	    long num = 0;  
	    for (int ix = 0; ix < 8; ++ix) {  
	        num <<= 8;  
	        num |= (byteNum[ix] & 0xff);  
	    }  
	    return String.valueOf(num);  
	}  
	
	public static void main(String[] args) {
		String str = getBase64("xxxxx6533").toString();
	      String str2 = getBase64("wx232444").toString();

		System.out.println(bytes2Long(str));
	      System.out.println(bytes2Long(str2));

//		String str1 = getFromBase64("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48cmVzcG9uc2U+PHJlc3VsdD4xPC9yZXN1bHQ+PGVycm9yRGVzYz7ml6DplJnor6/kv6Hmga88L2Vycm9yRGVzYz48ZXJyb3JDb2RlPkUwMDA8L2Vycm9yQ29kZT48YXNzaWduSWRzPjxhc3NpZ25JZD48YmlsbG5vPlRFU1QxMDI0MTQ0NDA8L2JpbGxubz48L2Fzc2lnbklkPjwvYXNzaWduSWRzPjwvcmVzcG9uc2U+");
		String str1 = getFromBase64(str);
		System.out.println(str1);
		
//		System.out.println(getBase64(MD5.md5(str2)));
		
	}
}