package com.xmy.socket.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	
	public static String md5(String str) {
		
		Logger logger = Logger.getLogger(MD5.class);
		
		try {
			
			//logger.info("test:"+str);
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		//logger.info("test:"+str);
		return str;
	}
	
	
	// 获得MD5摘要算法的 MessageDigest 对象
	private static MessageDigest _mdInst = null;
	private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static MessageDigest getMdInst() {
		if (_mdInst == null) {
			try {
				_mdInst = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return _mdInst;
	}

	public final static String encode(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 使用指定的字节更新摘要
			getMdInst().update(btInput);
			// 获得密文
			byte[] md = getMdInst().digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		
		String json="{\"area\":\"江苏省南京市栖霞区\", \"bank\":\"c4b8217558134deba53e49f3fd917bb2\", \"branch\":\"41d68a1576394d5682dc932fbb909ef1\", \"id\":\"1e9e0a2e0ce34afc9b47fbac7242de56\", \"idCardNumber\":\"341222199207156010\", \"idCardPicPath\":\"My/person/341222199207156010\", \"jobnum\":\"123123\", \"name\":\"123\", \"phone\":\"18756220755\", \"picPath\":\"My/person/341222199207156010/avatar.jpg\"}";
		String context = Base64Util.getBase64(json);
		System.out.println(context);
		System.out.println(Base64Util.getFromBase64(context));
		
		long time = 1490766502861L;
		System.out.println(time);
		
		String key = "123456";
		
		System.out.println(md5(context+key+time));
		
		
	}
}
