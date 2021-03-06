package com.qing.ac.analysis.template;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.qing.ac.analysis.model.UP;
import com.qing.ac.analysis.service.DataService;
import com.qing.ac.analysis.service.Response;

public class UPTemplate extends Template {
	private DataService dataService = new DataService();
	
	@Override
	public boolean deal(Response response, Document doc) throws Exception {
		UP up = new UP();
		up.setUrl(response.getUrl());
		if(saveAUP(up, doc))
			return true;
		
//		if(saveBUP(up, doc))
//			return true;
		
		return false;
	}
	
	boolean saveAUP(UP up, Document doc) {
		try {
			Element userInfo = doc.getElementById("block-user-left");
			if(userInfo == null) {
				return false;
			}
			
			up.setNick(userInfo.child(0).child(0).text());
			String uidHref = userInfo.child(0).child(0).attr("href");
			up.setUid(uidHref.substring(3, uidHref.indexOf(".")));
			up.setImg(userInfo.child(1).getElementsByTag("img").first().attr("src"));
			up.setPosts(Integer.valueOf(userInfo.child(4).child(1).child(0).text()));
			up.setFollowing(Integer.valueOf(userInfo.child(4).child(2).child(0).text()));
			up.setFollowers(Integer.valueOf(userInfo.child(4).child(3).child(0).text()));
			String sex = userInfo.child(5).child(1).text();
			up.setSex(sex.substring(sex.indexOf("：")+1));
			
			String loves = userInfo.child(5).child(2).text();
			up.setLoves(loves.substring(loves.indexOf("：")+1));
			
			String address = userInfo.child(5).child(3).text();
			up.setAddress(address.substring(address.indexOf("：")+1));
			
			String registTime = userInfo.child(5).child(4).text();
			up.setRegistTime(registTime.substring(registTime.indexOf("：")+1));
			
			String qq = userInfo.child(5).child(6).text();
			up.setQQ(qq.substring(qq.indexOf("：")+1));
			
			dataService.saveUP(up);
		} catch (Exception e) {
			System.out.println("saveAUP error " + up.getUrl());
			return false;
		}
		return true;
	}

	boolean saveBUP(UP up, Document doc) {
		try {
			Element hname = doc.getElementsByClass("h-name").first();
			if(hname == null)
				return false;
			
		} catch (Exception e) {
			System.out.println("saveBUP error " + up.getUrl());
			return false;
		}
		return true;
	}
}
