/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.ghkj.gaqcommons.untils;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;


/**
 * 支持SHA-1/MD5消息摘要的工具类.
 * 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * @author KVIUFF
 */
public class DigestsUtil {

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";
	
	public static final String HASH_ALGORITHM = "MD5";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private static SecureRandom random = new SecureRandom();

	/**
	 * 对输入字符串进行md5散列.
	 */
	public static byte[] md5(byte[] input) {
		return digest(input, MD5, null, 1);
	}
	public static byte[] md5(byte[] input, int iterations) {
		return digest(input, MD5, null, iterations);
	}
	
	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input) {
		return digest(input, SHA1, null, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) {
		return digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成随机的Byte[]作为salt.
	 * 
	 * @param numBytes byte数组的大小
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 对文件进行md5散列.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static byte[] digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * 给明文 加密
	 * 吴璇璇
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = DigestsUtil.generateSalt(SALT_SIZE);
		byte[] bytes = new byte[0];
		try {
			bytes = plainPassword.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] hashPassword = DigestsUtil.sha1(bytes, salt, HASH_INTERATIONS);
		return EncodesUtil.encodeHex(salt)+ EncodesUtil.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 * 吴璇璇
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = EncodesUtil.decodeHex(password.substring(0,16));
		byte[] bytes = new byte[0];
		try {
			bytes = plainPassword.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] hashPassword = DigestsUtil.sha1(bytes, salt, HASH_INTERATIONS);
		return password.equals(EncodesUtil.encodeHex(salt)+ EncodesUtil.encodeHex(hashPassword));
	}
	
	public static void main(String[] args) {
		System.out.println(entryptPassword("kviuff"));
		System.out.println(validatePassword("kviuff", "1b1e99f55f4162eca8632c3b16aab0294c5fac5ba3f20dc0eb41576a"));
	}
	
}
