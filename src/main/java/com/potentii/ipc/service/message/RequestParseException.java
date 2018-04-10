package com.potentii.ipc.service.message;

public class RequestParseException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public RequestParseException(Throwable e) {
		super(e);
	}

}
