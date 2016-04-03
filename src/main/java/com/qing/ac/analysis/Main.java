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
import com.qing.ac.analysis.service.HttpService;
import com.qing.ac.analysis.service.Request;
import com.qing.ac.analysis.service.Response;
import com.qing.ac.analysis.template.Template;
import com.qing.ac.analysis.template.UPTemplate;
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
//			DataService.PASSWORD = args[0].trim();
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
	
	private final static int LIMITS_OF_DEALINGURL = 10;
	
	private final static int THREAD_SIZE = 10;
	
	void run() throws Exception {
		System.out.println("start");
		// 请求数据
//		new Thread(() -> {
			Executor executor = Executors.newFixedThreadPool(THREAD_SIZE);
			while(true) {
				System.out.println("start " + amountOfDealingUrl);
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
				
				// 此处有个隐藏bug，当该url已经被分析过后，要更新url的状态，否则积累一定次数后会造成死循环
				if(dataService.checkRequested(request) > 0) {
					try {
						dataService.saveRequestedURL(request, true);
					} catch (Exception e3) {
						// TODO: handle exception
						e3.printStackTrace();
					}
					continue;
				}
				
				// 请求前保存请求的URL地址，防止重复请求
				try {
					dataService.saveRequestedURL(request, false);
				} catch (Exception e3) {
					// TODO: handle exception
					e3.printStackTrace();
				}
				System.out.println(request.getUrl());
				// 更新当前处理的请求数
				try {
					lock.lock();
					amountOfDealingUrl ++;
					if(amountOfDealingUrl >= LIMITS_OF_DEALINGURL) {
						try {
							System.out.println(request.getUrl() + " : waiting");
							condition.await();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch(Exception e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				} finally {
					lock.unlock();
				}
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
					
					Document doc = null;
					try {
						doc = Jsoup.parse(response.getFile(), "UTF-8");
						boolean hasTemplate = false;
						for(Template template : templateList) {
							try {
								if(template.deal(response, doc))
									hasTemplate = true;
							} catch (Exception e2) {
								e2.printStackTrace();
								// TODO
							}
						}
						// 分析完成
						dataService.updateRequestedUrl(hasTemplate, request);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						// 
						lock.lock();
						amountOfDealingUrl --;
						if(amountOfDealingUrl < LIMITS_OF_DEALINGURL) {
							try {
								condition.signalAll();
								System.out.println(request.getUrl() + " : done " + amountOfDealingUrl + " =: " + requestQueue.size());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} catch (Exception e3) {
						// TODO: handle exception
						e3.printStackTrace();
					} finally {
						lock.unlock();
					}
				});
			}
//		}).start();
	}
	
	void init() {
		String domain = "http://www.acfun.tv";
		final String urlKey[] = {"acfun", 
				"bilibili"};
//		dataService.createTable();
//		dataService.deleteData();
		
//		addRequest(new Request("http://www.acfun.tv"));
		addRequest(new Request("http://www.bilibili.com/video/av4237821/"));
//		.addRequest(new Request("http://www.acfun.tv/v/ac488387?from-baifendian"));
//		.addRequest(new Request("http://www.acfun.tv/v/ac1826245?from-baifendian"))
//		.addRequest(new Request("http://www.acfun.tv/v/ac1841389?from-baifendian"));
		
		URLTemplate urlTemplate = new URLTemplate(domain);
		urlTemplate.addURLFilter((url) -> {
			
			for(String keyword : urlKey) {
				if(url.contains(keyword))
					return false;
			}
			
			return true;
		});
		addTemplate(new VideoTemplate())
		.addTemplate(urlTemplate)
		.addTemplate(new UPTemplate());
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
