package com.qing.ac.analysis;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.qing.ac.analysis.service.Request;
import com.qing.ac.analysis.service.Response;

public class HttpService {
	private HttpClient httpClient = HttpClients.createSystem();
	
	public HttpService() {
		init();
	}
	
	private void init() {
	}
	
	void setHeader(HttpRequest request) {
		
	}

	public Response requset(Request request) throws Exception {
		HttpGet get = new HttpGet(request.getUrl());
		setHeader(get);
		HttpResponse response = httpClient.execute(get);

		Response result = new Response();
		result.setContent(EntityUtils.toString(response.getEntity(), "UTF-8"));
		result.setUrl(request.getUrl());
		return result;
	}
}
