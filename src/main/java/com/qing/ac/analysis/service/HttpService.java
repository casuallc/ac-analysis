package com.qing.ac.analysis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.qing.utils.WebUtils;

public class HttpService {
	private HttpClient httpClient = HttpClients.createSystem();
	
	private RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(10000).build();
	
	public HttpService() {
		init();
	}
	
	private void init() {
		
	}
	
	public static String USER_AGENT[] = {
		"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0",
		"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586"
	};
	public static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
	public static final String ACCEPT_ENCODING = "gzip, deflate";
	public static final String CONNECTION = "keep-alive";
	public static final String PRAGRAM = "no-cache";
	public static final String CACHE_CONTROL = "no-cache";
	public String host = "";
	
	private Random random = new Random(System.currentTimeMillis());
	
	void setHeader(HttpRequestBase request) {
//		request.setHeader("Host", "log.aixifan.com");
		request.setHeader("User-Agent", USER_AGENT[random.nextInt(USER_AGENT.length)]);
		request.setHeader("Accept", ACCEPT);
		request.setHeader("Accept-Language", ACCEPT_LANGUAGE);
		request.setHeader("Accept-Encoding", ACCEPT_ENCODING);
		request.setHeader("Connection", CONNECTION);
		request.setHeader("Pragram", PRAGRAM);
		request.setHeader("Cache-Control", CACHE_CONTROL);
	}

	public Response requset(Request request) throws Exception {
//		host = request.getUrl().substring(0, request.getUrl().length()-2).replace("http://", "").trim();
		
		Thread.sleep(5000 + random.nextInt(5000));
		HttpGet get = new HttpGet(WebUtils.delBlank(WebUtils.encodeUrl(request.getUrl())));
		get.setConfig(config);
		setHeader(get);
//		for(Header h : get.getAllHeaders()) {
//			System.out.println(h.getName() + ": " + h.getValue());
//		}
		HttpResponse response = httpClient.execute(get);

		Response result = new Response();
		File tempFile = File.createTempFile("temp"+random.nextInt(1000), ".tmp");
		response.getEntity().writeTo(new FileOutputStream(tempFile));
		result.setFile(tempFile);
//		result.setContent(EntityUtils.toString(response.getEntity(), "UTF-8"));
		result.setUrl(request.getUrl());
		
		return result;
	}
}
