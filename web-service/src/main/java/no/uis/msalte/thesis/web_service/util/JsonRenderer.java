package no.uis.msalte.thesis.web_service.util;

import spark.ResponseTransformer;

import com.google.gson.Gson;

public class JsonRenderer implements ResponseTransformer {
	private Gson gson = new Gson();
	
	@Override
	public String render(Object obj) throws Exception {
		return gson.toJson(obj);
	}
	
}
