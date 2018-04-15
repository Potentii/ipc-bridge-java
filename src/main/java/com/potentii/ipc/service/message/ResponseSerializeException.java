package com.potentii.ipc.service.message;


public class ResponseSerializeException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResponseSerializeException(Throwable e) {
        super(e);
    }

}