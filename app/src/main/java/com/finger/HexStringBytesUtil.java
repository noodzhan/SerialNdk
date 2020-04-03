package com.finger;

public class HexStringBytesUtil {

	//十六进制的字节数组转十六进制形式的字符串
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	//十六进制形式的字符串转十六进制的字节数据
	public static byte[] hex2byte(String hex) {
		String digital = "0123456789ABCDEF";
		String hex1 = hex.replace(" ", "");
		char[] hex2char = hex1.toCharArray();
		byte[] bytes = new byte[hex1.length() / 2];
		byte temp;
		for (int p = 0; p < bytes.length; p++) {
			temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
			temp += digital.indexOf(hex2char[2 * p + 1]);
			bytes[p] = (byte) (temp & 0xff);
		}
		return bytes;
	}




}
