package com.potentii;

import com.potentii.ipc.worker.api.IPCWorker;

public class Main{
	
	public static void main(String[] args){
		IPCWorker bridge = new IPCWorker(System.in, System.out);
		
		bridge.listen((req, res) -> {
			String operation = req.queryString("operation");
			
			res.text(operation);
		});
	}
	
}
