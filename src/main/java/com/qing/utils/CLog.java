package com.qing.utils;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author liuchangqing
 * @time 2016年2月9日上午10:20:28
 * @function 
 */
public class CLog {

	private Log log = null;
	private File logFile = null;
	
	public CLog(Class clazz, boolean useSystemp) {
		if(!useSystemp)
			log = LogFactory.getLog(clazz);
	}
	
	public void setLogFile(File file) {
		this.logFile = file;
	}
	
	public void info(String info) {
		if(log == null) {
			if(logFile == null)
				System.out.println(info);
			else {
				try {
					FileUtils.saveToFile("\n" + info, logFile, true);
				} catch (Exception e) {
					System.out.println("save log failed: " + info);
				}
			}
		} else {
			log.info(info);
		}
	}
}
