package com.qing.ac.analysis.template;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.qing.ac.analysis.model.UP;
import com.qing.ac.analysis.model.Video;
import com.qing.ac.analysis.service.DataService;
import com.qing.ac.analysis.service.Response;

public class VideoTemplate extends Template {
	private DataService dataService = new DataService();

	@Override
	public boolean deal(Response response, Document doc) throws Exception {
		Video video = new Video();
		video.setUrl(response.getUrl());
		if(saveAVideo(video, doc))
			return true;
		
		if(saveBVideo(video, doc))
			return true;
		
		return false;
	}

	boolean saveAVideo(Video video, Document doc) throws Exception {
		try {
			Element author = doc.getElementById("btn-follow-author");
			video.setTitle(doc.getElementById("txt-title-view").text());
			video.setIntro(doc.getElementById("block-info-view").child(0).child(2).text());
			video.setPublishTime(doc.getElementsByClass("time").text());
			video.setUid(author.attr("data-uid"));
			dataService.saveVideo(video);
			return true;
		} catch (Exception e) {
			System.out.println("saveAVideo error " + video.getUrl());
			return false;
		}
	}
	
	boolean saveBVideo(Video video, Document doc) throws Exception {
		try {
			Element info = doc.getElementsByClass("info").first();
			video.setTitle(info.getElementsByClass("v-title").first().child(0).attr("title"));
			video.setPublishTime(info.getElementsByTag("time").first().child(0).text());
			video.setViews(formatToNum(info.getElementById("dianji").text()));
			video.setDanmu(formatToNum(info.getElementById("dm_count").text()));
			video.setYb(formatToNum(info.getElementById("v_ctimes").text()));
			video.setCollections(formatToNum(info.getElementById("stow_count").text()));
			
			Element upinfo = doc.getElementsByClass("upinfo").first();
			video.setUid(upinfo.child(1).getElementsByTag("tag").last().attr("mid"));
			dataService.saveBVideo(video);
			
			// 保存up信息
			UP up = new UP();
			up.setUid(video.getUid());
			Element url = upinfo.child(1).getElementsByClass("usname").first().child(0);
			up.setUrl(url.attr("href"));
			up.setNick(url.attr("card"));
			
			up.setIntro(upinfo.getElementsByClass("sign").text());
			Element up_video_message = upinfo.getElementsByClass("up-video-message").first();
			String posts = up_video_message.child(0).text();
			up.setFollowers(Long.valueOf(posts.substring(posts.indexOf("：")+1)));
			String followers = up_video_message.child(1).text();
			up.setFollowers(Long.valueOf(followers.substring(followers.indexOf("：")+1)));
			up.setImg(upinfo.getElementById("r-info-rank").child(0).child(0).attr("src"));
			dataService.saveBUP(up);
			return true;
		} catch (Exception e) {
			System.out.println("saveBVideo error " + video.getUrl());
//			e.printStackTrace();
			return false;
		}
	}
	
	long formatToNum(String str) {
		str = str.trim();
		long result = 0;
		if(str.contains("万"))
			result = Long.valueOf(str.replace("万", "")) * 10000;
		else
			result = Long.valueOf(str);
		return result;
	}
}
