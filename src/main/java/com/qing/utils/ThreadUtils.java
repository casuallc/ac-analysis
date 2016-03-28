package com.qing.utils;

/**
 * @author liuchangqing
 * @time 2016年2月7日下午7:03:25
 * @function 
 */
public class ThreadUtils {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
