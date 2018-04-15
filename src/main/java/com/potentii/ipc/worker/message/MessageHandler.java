package com.potentii.ipc.worker.message;

@FunctionalInterface
public interface MessageHandler {
	void handle(IncomingMessage req, OutgoingMessage res);
}
