package com.potentii.ipc.service.message;

import com.potentii.ipc.Processor;
import com.sun.istack.internal.Nullable;

public class RequestProcessor implements Processor<IPCRequest, IPCResponse> {
	@Nullable
	private final IPCRequestHandler requestHandler;
	
	
	
	public RequestProcessor(@Nullable IPCRequestHandler requestHandler) {
		super();
		this.requestHandler = requestHandler;
	}
	
	

	@Override
	public IPCResponse process(IPCRequest request){
		IPCResponse response = new IPCResponse(request.id());
		
		if(requestHandler != null){
			try{
				requestHandler.handle(request, response);
			} catch(Exception e){
				response.error(e);
			}
		}
		
		return response;
	}

}
