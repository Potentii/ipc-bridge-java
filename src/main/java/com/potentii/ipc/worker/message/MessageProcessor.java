package com.potentii.ipc.worker.message;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.potentii.ipc.Processor;
import com.sun.istack.internal.NotNull;

public class MessageProcessor implements Processor<String, String>{

	private final ObjectMapper mapper;
	private final Processor<IncomingMessage, OutgoingMessage> requestProcessor;

	

	public MessageProcessor(RequestProcessor requestProcessor) {
		super();
		this.requestProcessor = requestProcessor;

		// *Creating a new JSON object mapper:
		mapper = new ObjectMapper();
	}



	private IncomingMessage parseRequest(final String jsonRequest) throws RequestParseException{
		try{
			// *Parsing the request object from JSON:
			return mapper.readValue(jsonRequest, IncomingMessage.class);
		} catch(IOException e){
			throw new RequestParseException(e);
		}
	}


	private String serializeResponse(final OutgoingMessage response) throws ResponseSerializeException{
		try{
			// *Returning the serialized response as JSON:
			return mapper.writeValueAsString(response);
		} catch(IOException e){
			throw new RequestParseException(e);
		}
	}


	/**
	 * Processes a message and provide a response for it
	 * @param jsonRequest The raw JSON request body
	 * @return The response for the request
	 * @throws RequestParseException
	 * @throws ResponseSerializeException
	 */
	@Override
	public String process(@NotNull final String jsonRequest) throws RequestParseException, ResponseSerializeException {

		// *Parsing the request object from JSON:
		IncomingMessage request = parseRequest(jsonRequest);

		// *Getting the processed response:
		OutgoingMessage response = requestProcessor.process(request);

		// *Returning the serialized response as JSON:
		return serializeResponse(response);
	}

}
