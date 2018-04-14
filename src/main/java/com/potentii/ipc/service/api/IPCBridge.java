package com.potentii.ipc.service.api;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.potentii.ipc.Processor;
import com.potentii.ipc.service.message.IPCRequestHandler;
import com.potentii.ipc.service.message.MessageProcessor;
import com.potentii.ipc.service.message.RequestParseException;
import com.potentii.ipc.service.message.RequestProcessor;

public class IPCBridge{
	private boolean listening;
	private final InputStream source;
	private final PrintStream target;
	private static final String END_MESSAGE_DELIMITER = "#msg-end";
	private static final Pattern MESSAGE_DELIMITER_REGEX = Pattern.compile("\\n" + END_MESSAGE_DELIMITER, Pattern.MULTILINE);
	
	
	
	public IPCBridge(InputStream source, PrintStream target) {
		super();
		this.source = source;
		this.target = target;
	}
	
	
	
	private String processInput(String input, Processor<String, String> messageProcessor) {
		String responseJson;
		
		try {
			responseJson = messageProcessor.process(input);
		} catch (RequestParseException e) {
			responseJson = "{\"error\":\"Not expected error\"}"; // TODO
		}
		
		responseJson += "\n" + END_MESSAGE_DELIMITER + "\n";
		
		return responseJson;
	}
	


	/**
	 * IPCRequest {
	 *    "id": "a7bf6a-eb6ffa",
	 *    "query": {
	 *       "operation": "123",
	 *       "def": 456,
	 *       "ghi": null,
	 *       "jkl": "hello world"
	 *    },
	 *    "content": ""
	 * }
	 * #msg-end
	 * 
	 * IPCResponse {
	 *    "id": "a7bf6a-eb6ffa",
	 *    "query": {
	 *       "abc": 123,
	 *       "def": 456,
	 *       "ghi": null,
	 *       "jkl": "hello world"
	 *    },
	 *    "content": {},
	 *    "error": {}
	 * }
	 * #msg-end
	 */
	public void listen(final IPCRequestHandler requestHandler){
		listening = true;
		
		// *Initializing the message processor using the given request handler implementation:
		final Processor<String, String> messageProcessor = new MessageProcessor(new RequestProcessor(requestHandler));
		
		// *Setting up the scanner that will wait for messages on 'source':
		final Scanner scanner = new Scanner(source);
		// *Using a delimiter, to separate each message:
		scanner.useDelimiter(MESSAGE_DELIMITER_REGEX);
		
		// *Starting the listening loop:
		while(listening){

			final String inputData = scanner.next();
			
			target.print(processInput(inputData, messageProcessor));
		}
		
		scanner.close();
	}
	
	
		
	public void stopListening(){
		// *It doesn't necessarily will stop listening immediately:
		listening = false;
	}



	public boolean isListening() {
		return listening;
	}

	public InputStream getSource() {
		return source;
	}
	
}
