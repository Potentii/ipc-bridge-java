package com.potentii;

import com.potentii.ipc.worker.api.IPCWorker;

import java.io.IOException;

public class Main{
	
	public static void main(String[] args) throws IOException {
		IPCWorker bridge = new IPCWorker(System.in, System.out);
		
		bridge.listen((req, res) -> {
			String operation = req.queryString("operation");
			
			res.text(operation);
		});
	}
	
}
