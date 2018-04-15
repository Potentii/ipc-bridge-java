package com.potentii.ipc.worker.message;

public class RequestParseException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public RequestParseException(Throwable e) {
		super(e);
	}

}
