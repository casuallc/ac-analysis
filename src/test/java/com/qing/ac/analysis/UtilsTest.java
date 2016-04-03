package com.qing.ac.analysis;

import com.qing.utils.WebUtils;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

	public void testWebUtilsEncodeUrl() {
		String url = " http://www.acfun.tv/member/#area=mail-new;username=第三把利剑出鞘;";
		System.out.println(WebUtils.encodeUrl(url));
		
		url = " http://www.acfun.tv/search/#query=GREEN WORLDZ ";
		System.out.println(WebUtils.delBlank(url));
	}
}
