package net.doyouhike.app.wildbird.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String Encode(String arg) {
		String cacheKey = null;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(arg.getBytes());
			cacheKey = byteToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO: handle exception
			cacheKey = String.valueOf(arg.hashCode());
		}
		return cacheKey;
	}

	private static String byteToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0xFF & data[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}


	private static byte[] base64decode(byte[] bytes){
		return Base64.decode(bytes,Base64.DEFAULT);
	}
}