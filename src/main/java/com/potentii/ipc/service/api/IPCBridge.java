package com.potentii.ipc.service.api;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.potentii.ipc.Processor;
import com.potentii.ipc.service.message.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class IPCBridge{
	/**
	 * Whether or not this bridge is listening to requests
	 */
	private boolean listening;

	@NotNull
	private final InputStream source;
	@Nullable
	private final PrintStream target;

	private static final String END_MESSAGE_DELIMITER = "#msg-end";
	private static final Pattern MESSAGE_DELIMITER_REGEX =
			Pattern.compile("\\n" + END_MESSAGE_DELIMITER, Pattern.MULTILINE);



	/**
	 * Prepares a new inter-process communication bridge
	 * To start the communication flow, use the {@code listen(IPCRequestHandler)} method of the returned instance
	 * @param source The origin of the messages, that is, the stream that the other process will write the
	 *                  requests to (e.g. In most cases it will be {@code System.in})
	 * @param target The destination of the messages, that is, the stream that the other process will read the
	 *                  responses from (e.g. In most cases it will be {@code System.out})
	 */
	public IPCBridge(@NotNull InputStream source, @Nullable PrintStream target) {
		super();
		if(source == null)
			throw new IllegalArgumentException("The source stream must not be null");

		this.source = source;
		this.target = target;
	}
	
	
	
	private String processInput(String input, Processor<String, String> messageProcessor) {
		String responseJson;
		
		try {
			responseJson = messageProcessor.process(input);
		} catch (RequestParseException e) {
			responseJson = "{\"error\":{\"message\":\"Error while parsing the request\"}}";
		} catch (ResponseSerializeException e) {
			responseJson = "{\"error\":{\"message\":\"Error while serializing the response\"}}";
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
	public void listen(@Nullable final IPCRequestHandler requestHandler){
		// *Signaling that this bridge have started listening to requests:
		listening = true;

		// *Initializing the processor of the requests that will be received, using the given handler implementation:
		final RequestProcessor requestProcessor = new RequestProcessor(requestHandler);
		// *Initializing the unit that will be responsible to process each raw message:
		final Processor<String, String> messageProcessor = new MessageProcessor(requestProcessor);
		
		// *Setting up the scanner that will wait for messages on the 'source' stream:
		final Scanner scanner = new Scanner(source);
		// *Using the default delimiter rule, to separate each of the raw messages:
		scanner.useDelimiter(MESSAGE_DELIMITER_REGEX);
		
		// *Starting the listening loop:
		while(listening){

			final String inputData = scanner.next();

			target.print(processInput(inputData, messageProcessor));
		}
	}



	private void insertStopSignalToSource(final InputStream source){
		throw new UnsupportedOperationException();
	}

	private void insertStopSignalToSource(){
		insertStopSignalToSource(source);
	}

	public void stopListening(){
		// *It doesn't necessarily will stop listening immediately:
		listening = false;
		insertStopSignalToSource();
	}



	public boolean isListening() {
		return listening;
	}

	public InputStream getSource() {
		return source;
	}

	public PrintStream getTarget() {
		return target;
	}
}
