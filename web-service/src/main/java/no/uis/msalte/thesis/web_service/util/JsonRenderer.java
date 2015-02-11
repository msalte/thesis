package no.uis.msalte.thesis.web_service.util;

import spark.ResponseTransformer;

import com.google.gson.Gson;

public class JsonRenderer implements ResponseTransformer {
	public static final Gson RENDERER = new Gson();
	
	@Override
	public String render(Object obj) throws Exception {
		return RENDERER.toJson(obj);
	}
	
}
