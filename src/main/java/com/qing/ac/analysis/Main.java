package com.qing.ac.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.qing.ac.analysis.service.DataService;
import com.qing.ac.analysis.service.Request;
import com.qing.ac.analysis.service.Response;
import com.qing.ac.analysis.template.Template;
import com.qing.ac.analysis.template.URLTemplate;
import com.qing.ac.analysis.template.VideoTemplate;

/**
 * 解析acfun中的数据
 * 获取视频信息（基本信息、发布者）
 * 获取up主信息（粉丝数、发布文章数、个人信息）
 * @author liuchangqing
 * @time 2016年3月27日下午5:39:19
 * @function
 */
public class Main {
	
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public static void main(String[] args) {
		try {
			Main main = new Main();
			main.init();
			main.run();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// 请求队列
	private Queue<Request> requestQueue = new LinkedBlockingQueue<>();
	// 处理http请求
	private HttpService httpService = new HttpService();
	// 处理请求结果列表
	private List<Template> templateList = new ArrayList<>();
	
	// 操作数据库
	private DataService dataService = new DataService();
	
	private int amountOfDealingUrl = 0;
	
	void run() throws Exception {
		System.out.println("start");
		// 请求数据
		new Thread(() -> {
			Executor executor = Executors.newFixedThreadPool(10);
			while(true) {
				final Request request = requestQueue.poll();
				if(request == null) {
					try {
						dataService.listUrl(requestQueue);
					} catch (Exception e) {
						// TODO
						e.printStackTrace();
					}
					continue;
				}
				
				if(dataService.checkRequested(request) != 0) {
					continue;
				}
				
				// 请求前保存请求的URL地址，防止重复请求
				try {
					dataService.saveRequestedURL(request);
				} catch (Exception e3) {
					// TODO: handle exception
					e3.printStackTrace();
				}
				
				// 更新当前处理的请求数
				lock.lock();
				amountOfDealingUrl ++;
				if(amountOfDealingUrl >= 10) {
					try {
						condition.await();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				lock.unlock();
				System.out.println(request.getUrl() + " : dealing");
				
				executor.execute(() -> {
					Response response = null;
					try {
						response = httpService.requset(request);
						
					} catch (Exception e) {
						e.printStackTrace();
						// TODO
						return;
					}
					
					Document doc = Jsoup.parse(response.getContent());
					for(Template template : templateList) {
						try {
							template.deal(response, doc);
						} catch (Exception e2) {
							e2.printStackTrace();
							// TODO
						}
					}
					lock.lock();
					amountOfDealingUrl --;
					if(amountOfDealingUrl < 10) {
						try {
							condition.signalAll();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					lock.unlock();
				});
			}
		}).start();
		System.out.println("end");
	}
	
	void init() {
		String domain = "http://www.acfun.tv";
		
		addRequest(new Request("http://www.acfun.tv"));
//		.addRequest(new Request("http://www.acfun.tv/v/ac488387?from-baifendian"))
//		.addRequest(new Request("http://www.acfun.tv/v/ac1826245?from-baifendian"))
//		.addRequest(new Request("http://www.acfun.tv/v/ac1841389?from-baifendian"));
		
		URLTemplate urlTemplate = new URLTemplate(domain);
		urlTemplate.addURLFilter((url) -> {
			if(url == null || url.equals(""))
				return true;
			
			if(!url.startsWith(domain+"/v/"))
				return true;
			
			return false;
		});
		addTemplate(new VideoTemplate())
		.addTemplate(urlTemplate);
	}
	
	public Main addRequest(Request request) {
		requestQueue.add(request);
		return this;
	}
	
	public Main addTemplate(Template template) {
		templateList.add(template);
		return this;
	}
}
