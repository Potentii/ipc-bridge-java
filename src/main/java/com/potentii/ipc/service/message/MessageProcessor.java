package com.potentii.ipc.service.message;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potentii.ipc.Processor;

public class MessageProcessor implements Processor<String, String>{

	private final ObjectMapper mapper;
	private final Processor<IPCRequest, IPCResponse> requestProcessor;

	

	public MessageProcessor(RequestProcessor requestProcessor) {
		super();
		this.requestProcessor = requestProcessor;

		// *Creating a new JSON object mapper:
		mapper = new ObjectMapper();
	}



	private IPCRequest parseRequest(final String jsonRequest) throws RequestParseException{
		try{
			// *Parsing the request object from JSON:
			return mapper.readValue(jsonRequest, IPCRequest.class);
		} catch(IOException e){
			throw new RequestParseException(e);
		}
	}


	private String serializeResponse(final IPCResponse response) throws ResponseSerializeException{
		try{
			// *Returning the serialized response as JSON:
			return mapper.writeValueAsString(response);
		} catch(IOException e){
			throw new RequestParseException(e);
		}
	}


	@Override
	public String process(final String jsonRequest) throws RequestParseException, ResponseSerializeException {

		// *Parsing the request object from JSON:
		IPCRequest request = parseRequest(jsonRequest);

		// *Getting the processed response:
		IPCResponse response = requestProcessor.process(request);

		// *Returning the serialized response as JSON:
		return serializeResponse(response);
	}

}
