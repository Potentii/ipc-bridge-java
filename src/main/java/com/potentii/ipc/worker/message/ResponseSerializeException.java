package com.potentii.ipc.worker.message;


public class ResponseSerializeException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResponseSerializeException(Throwable e) {
        super(e);
    }

}