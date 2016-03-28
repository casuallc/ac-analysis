package com.qing.ac.analysis.template;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.qing.ac.analysis.service.DataService;
import com.qing.ac.analysis.service.Response;

/**
 * 普通标签
 * @author liuchangqing
 * @time 2016年3月28日下午2:31:14
 * @function
 */
public class URLTemplate extends Template {
	private String domain;
	
	private DataService dataService = new DataService();
	
	private List<URLFilter> urlFilterList;
	
	public URLTemplate(String domain) {
		this.domain = domain;
	}

	@Override
	public void deal(Response response, Document doc) throws Exception {
		// 获取A标签中的url
		Elements eles = doc.getElementsByTag("a");
		List<String> urlList = new ArrayList<>();
		eles.forEach((e) -> {
			String href = e.attr("href");
			if(StringUtil.isBlank(href))
				return;
			
			if(href.startsWith("/"))
				href = domain + href;
			
			// 过滤url
			urlFilter(urlList, href);
		});
		
		dataService.saveURL(urlList);
	}

	/**
	 * 过滤出一些url地址
	 * @param filter
	 * @return
	 */
	public URLTemplate addURLFilter(URLFilter filter) {
		if(urlFilterList == null)
			urlFilterList = new ArrayList<>();
		urlFilterList.add(filter);
		return this;
	}
	
	public interface URLFilter {
		public boolean pass(String url);
	}
	
	/**
	 * 根据用户添加的Filter过滤出URL
	 * @param urlList
	 * @param url
	 */
	void urlFilter(List<String> urlList, final String url) {
		urlFilterList.forEach((filter) -> {
			if(filter.pass(url)) 
				return;
			
			urlList.add(url);
		});
	}
}
