package com.qing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author liuchangqing
 * @time 2016年2月7日下午12:30:09
 * @function 
 */
public class FileUtils {
	
	public static boolean createDirectory(File file) {
		try {
			if(file == null)
				return false;
			
			if(!file.exists()) {
				return file.mkdirs();
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 读完文件后，默认关闭读入流。
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String getFileContent(String fileName) throws Exception {
		File file = new File(fileName);
		InputStream is = new FileInputStream(file);
		return getFileContent(is);
	}
	
	/**
	 * 读完文件后，默认关闭读入流。
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String getFileContent(InputStream is) throws Exception {
		return getContent(is, true);
	}
	
	public static String getContent(InputStream is, boolean closed) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		if(closed)
			br.close();
		return sb.toString();
	}
	
	/**
	 * 覆盖原有文件
	 * @param content
	 * @param file
	 * @throws Exception
	 */
	public static void saveToFile(String content, File file) throws Exception {
		saveToFile(content, file, false);
	}
	
	/**
	 * 以UTF-8的格式存入字符串到文件中
	 * @param content
	 * @param file
	 * @param append
	 * @throws Exception
	 */
	public static void saveToFile(String content, File file, boolean append) throws Exception {
		FileOutputStream fos = new FileOutputStream(file, append);
		fos.write(content.getBytes(Charset.forName("UTF-8")));
		fos.flush();
		fos.close();
	}
	
	/**
	 * 得到文件目录名称，是目录，不是文件名称
	 * @param strs
	 * @return
	 */
	public static String facFilePath(String ...strs) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<strs.length; i++) {
			
			String array[] = strs[i].split("//");
			for(String s : array) {
				sb.append(s).append(File.separator);
			}
		}
		return sb.toString();
	}
}
