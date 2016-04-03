package com.qing.ac.analysis.template;

import org.jsoup.nodes.Document;

import com.qing.ac.analysis.service.Response;

public abstract class Template {

	public abstract boolean deal(Response response, Document doc) throws Exception;
}
