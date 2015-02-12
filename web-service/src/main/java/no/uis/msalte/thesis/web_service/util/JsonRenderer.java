package no.uis.msalte.thesis.web_service.util;

import spark.ResponseTransformer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRenderer implements ResponseTransformer {
	public static final Gson RENDERER = new GsonBuilder().disableHtmlEscaping().create();
	
	@Override
	public String render(Object obj) throws Exception {
		return RENDERER.toJson(obj);
	}
	
}
