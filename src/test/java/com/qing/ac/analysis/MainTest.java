package com.qing.ac.analysis;

import com.qing.ac.analysis.service.Request;
import com.qing.ac.analysis.template.URLTemplate;
import com.qing.ac.analysis.template.VideoTemplate;

import junit.framework.TestCase;

public class MainTest extends TestCase {

	public void testRequest() throws Exception {
		String domain = "http://www.acfun.tv";
		
		Main main = new Main();
		main.addRequest(new Request("http://www.acfun.tv/v/ac2629120"));
//		.addRequest(new Request("http://www.acfun.tv/v/ac488387?from-baifendian"))
//		.addRequest(new Request("http://www.acfun.tv/v/ac1826245?from-baifendian"))
//		.addRequest(new Request("http://www.acfun.tv/v/ac1841389?from-baifendian"));
		
		URLTemplate urlTemplate = new URLTemplate(domain);
		urlTemplate.addURLFilter((url) -> {
			if(url == null || url.equals(""))
				return true;
			
			if(!url.startsWith(domain))
				return true;
			
			return false;
		});
		main.addTemplate(new VideoTemplate())
		.addTemplate(urlTemplate);
		
		main.run();
		Thread.sleep(10000);
	}
}
