package com.qing.utils;

public class WebUtils {

	public static String encodeUrl(String url) {
		int start = 0;
		int end = url.length();

		for(int i=0; i<url.length(); i++) {
			if(url.charAt(i) == 'h')
				break;
			start ++;
		}
		
		// 去除字符尾的空格
		for(int i=url.length()-1; i>0; i--) {
			if(url.charAt(i) == '\u0020' || url.charAt(i) == '\u3000')
				end --;
			else 
				break;
		}
		
		return url.substring(start, end);
	}
	
	public static String delBlank(String url) {
		return url.replaceAll("\u0020", "").replaceAll("\u3000", "");
	}
}
