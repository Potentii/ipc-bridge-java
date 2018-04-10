package com.potentii.ipc.service.message;

@FunctionalInterface
public interface IPCRequestHandler {
	void handle(IPCRequest req, IPCResponse res);
}
