package com.potentii.ipc.service.message;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class IPCRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Map<String, Object> query;
	private String content;
	
	
		
	public String id() {
		return getId();
	}
	
	public Map<String, Object> headers(){
		return query;
	}
	public Object get(String key) {
		return (headers() == null) 
				? null
				: headers().get(key);
	}
		
	public String text() {
		return content;
	}
	public <T> T json(Class<T> clazz) throws RequestParseException {
		try {
			return (content == null) 
					? null 
					: new ObjectMapper().readValue(content, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RequestParseException(e);
		}
	}
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getQuery() {
		return query;
	}
	public void setQuery(Map<String, Object> query) {
		this.query = query;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
