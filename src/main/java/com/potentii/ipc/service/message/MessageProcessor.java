package com.potentii.ipc.service.message;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potentii.ipc.Processor;

public class MessageProcessor implements Processor<String, String>{	
	private final Processor<IPCRequest, IPCResponse> requestProcessor;

	

	public MessageProcessor(RequestProcessor requestProcessor) {
		super();
		this.requestProcessor = requestProcessor;
	}



	@Override
	public String process(String jsonRequest) throws RequestParseException {
		try{
			ObjectMapper mapper = new ObjectMapper();
			
			// *Parsing the request object from JSON:
			IPCRequest request = mapper.readValue(jsonRequest, IPCRequest.class);
			
			// *Getting the processed response:
			IPCResponse response = requestProcessor.process(request);
			
			// *Returning the serialized response as JSON:
			return mapper.writeValueAsString(response);
		} catch(IOException e){
			throw new RequestParseException(e);
		}
	}

}
