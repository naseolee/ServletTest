package com.example.util;


public class ServletUtils {
	
	private ServletUtils() {
		// 아무것도 안함.
	}
	
	public static String deleteExt(String str, int extLength) {
		return str.substring(0, str.length() - extLength);
	}

}
