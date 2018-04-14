package com.potentii.ipc.service.message;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IPCResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Map<String, String> query;
	private Throwable error;
	private Object content;
	
	
	
	public IPCResponse(String id) {
		super();
		this.id = id;
		this.query = new HashMap<>();
	}
	
	
	
	public String id() {
		return id;
	}
	
	public String get(String key) {
		return query.get(key);
	}
	
	public void error(Throwable error) {
		this.error = error;
	}
	
	public void text(String content) {
		this.content = content;
	}
	public <T> void json(T obj) throws IOException {		
		content = (obj == null) 
				? null 
				: new ObjectMapper().writeValueAsString(obj);
	}
	
		
	
	public String getId() {
		return id;
	}
	
	public Throwable getError() {
		return error;
	}

	public Object getContent() {
		return content;
	}
}
