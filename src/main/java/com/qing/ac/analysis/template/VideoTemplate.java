package com.qing.ac.analysis.template;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.qing.ac.analysis.model.Video;
import com.qing.ac.analysis.service.DataService;
import com.qing.ac.analysis.service.Response;

public class VideoTemplate extends Template {

	@Override
	public void deal(Response response, Document doc) throws Exception {
		
		Element author = doc.getElementById("btn-follow-author");
		if(author == null)
			return;
//		System.out.println(author.attr("data-name"));
//		System.out.println(author.attr("data-uid"));
//		
//		System.out.println(doc.getElementById("txt-title-view").text()); // title
//		
//		System.out.println(doc.getElementById("block-info-view").child(0).child(2).text()); // intro
//		
//		System.out.println(doc.getElementsByClass("time").text()); // publish time
//		
//		System.out.println(doc.getElementById("txt-info-title").child(0).child(1).text()); // views
//		System.out.println(doc.getElementById("txt-info-title").child(1).child(1).text()); // comments
//		System.out.println(doc.getElementById("btn-banana-toolbar").child(2).text()); // bananas
		
		Video video = new Video();
		video.setTitle(doc.getElementById("txt-title-view").text());
		video.setIntro(doc.getElementById("block-info-view").child(0).child(2).text());
		video.setPublishTime(doc.getElementsByClass("time").text());
		video.setUrl(response.getUrl());
		video.setUid(author.attr("data-uid"));
		new DataService().saveVideo(video);
	}

}
