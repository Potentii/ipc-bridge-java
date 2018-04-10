package com.potentii.ipc;

public interface Processor<T,U>{
	U process(T t);
}
