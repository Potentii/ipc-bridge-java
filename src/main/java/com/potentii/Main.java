package com.potentii;

import com.potentii.ipc.service.api.IPCBridge;

public class Main{
	
	public static void main(String[] args){
		IPCBridge bridge = new IPCBridge(System.in, System.out);
		
		bridge.listen((req, res) -> {
			String operation = String.valueOf(req.headers().get("operation"));
			
			res.text(operation);
		});
	}
	
}
