package com.mbl.base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class BackAES {

	/**
	 * ���ܽ���㷨
	 */
	// private static String sKey = "1234567890123456";
	private static String ivParameter = "1234567890123456";//Ĭ��ƫ��

	private static String WAYS = "AES";
	private static String MODE = "";
	private static boolean isPwd = false;
	private static String ModeCode = "PKCS5Padding";
	private static int type = 0;// Ĭ��
	
	private static int pwdLenght=16;
	private static  String val="0";

	public static String selectMod(int type) {
		// ECB("ECB", "0"), CBC("CBC", "1"), CFB("CFB", "2"), OFB("OFB", "3");
		switch (type) {
		case 0:
			isPwd = false;
			MODE = WAYS + "/" + AESType.ECB.key() + "/" + ModeCode;

			break;
		case 1:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CBC.key() + "/" + ModeCode;
			break;
		case 2:
			isPwd = true;
			MODE = WAYS + "/" + AESType.CFB.key() + "/" + ModeCode;
			break;
		case 3:
			isPwd = true;
			MODE = WAYS + "/" + AESType.OFB.key() + "/" + ModeCode;
			break;

		}

		return MODE;

	}
/********************************����һ���ܳױ���Ϊ16λ**********************************************/
	// ����
	public static byte[] encrypt(String sSrc, String sKey, int type)
			throws Exception {
		sKey=toMakekey(sKey, pwdLenght, val);
		Cipher cipher = Cipher.getInstance(selectMod(type));
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);

		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
		if (isPwd == false) {// ECB ��������
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		}

		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return Base64.encode(encrypted);// �˴�ʹ��BASE64��ת�롣
	}

	// ����
	public static String decrypt(String sSrc, String sKey, int type)
			throws Exception {
			sKey=toMakekey(sKey, pwdLenght, val);
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, WAYS);
			Cipher cipher = Cipher.getInstance(selectMod(type));
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			if (isPwd == false) {// ECB ��������
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			}
			byte[] encrypted1 = Base64.decode(sSrc.getBytes());// ����base64����
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	
	//key 
	public static String toMakekey(String str,int strLength,String val){
		
		int strLen=str.length();
		if (strLen<strLength) {
			while (strLen<strLength) {
				StringBuffer  buffer=new StringBuffer();
				buffer.append(str).append(val);
				str=buffer.toString();
				strLen=str.length();
			}
		}
		return str;
	}

	
/***********************************�ڶ���***********************************************/
	
	
	public static byte[] newencrypt(String content, String password) {
        try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128, new SecureRandom(password.getBytes()));
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// ����AES���ܱ�����
                byte[] byteContent = content.getBytes("UTF-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��AES����
                byte[] result = cipher.doFinal(byteContent);
                return result; // AES���ܽ��
        } catch (Exception e) {
                e.printStackTrace();
        }
        return null;
}
	/*
	  * @param content  ����������,��ʽΪbyte����
      * @param password AES����ʹ�õ���Կ
      * @return
      */
     public static byte[] newdecrypt(byte[] content, String password) {
             try {
                      KeyGenerator kgen = KeyGenerator.getInstance("AES");
                      kgen.init(128, new SecureRandom(password.getBytes()));
                      SecretKey secretKey = kgen.generateKey();
                      byte[] enCodeFormat = secretKey.getEncoded();
                      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                      Cipher cipher = Cipher.getInstance("AES");// ����AES���ܱ�����
                     cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��AES����
                     byte[] result = cipher.doFinal(content);
                     return result; // �õ�AES���ܽ��
             } catch (Exception e) {
                     e.printStackTrace();
             }
             return null;
     }
     
     /**��������ת����16����
      * @param buf
      * @return
      */
     public static String parseByte2HexStr(byte buf[]) {
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < buf.length; i++) {
                     String hex = Integer.toHexString(buf[i] & 0xFF);
                     if (hex.length() == 1) {
                             hex = '0' + hex;
                     }
                     sb.append(hex.toUpperCase());
             }
             return sb.toString();
     }
     /**java��16����ת��Ϊ������
      * @param hexStr
      * @return
      */
     public static byte[] parseHexStr2Byte(String hexStr) {
             if (hexStr.length() < 1)
                     return null;
             byte[] result = new byte[hexStr.length()/2];
             for (int i = 0;i< hexStr.length()/2; i++) {
                     int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                     int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                     result[i] = (byte) (high * 16 + low);
             }
             return result;
     }
	
}
