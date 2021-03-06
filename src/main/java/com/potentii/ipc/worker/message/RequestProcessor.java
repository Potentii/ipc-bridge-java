package com.potentii.ipc.worker.message;

import com.potentii.ipc.Processor;
import org.jetbrains.annotations.Nullable;

public class RequestProcessor implements Processor<IncomingMessage, OutgoingMessage> {
	@Nullable
	private final MessageHandler requestHandler;
	
	
	
	public RequestProcessor(@Nullable MessageHandler requestHandler) {
		super();
		this.requestHandler = requestHandler;
	}
	
	

	@Override
	public OutgoingMessage process(IncomingMessage request){
		OutgoingMessage response = new OutgoingMessage(request.id()); // TODO check if ID is null
		
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
