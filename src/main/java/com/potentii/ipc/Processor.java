package com.potentii.ipc;

@FunctionalInterface
public interface Processor<T,U>{
	U process(T t);
}
