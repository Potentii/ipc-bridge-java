package com.potentii.ipc.worker.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.potentii.ipc.Processor;
import com.potentii.ipc.worker.message.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



/**
 * @author Guilherme Reginaldo Ruella
 */
public class IPCWorker{
	/**
	 * Whether or not this bridge is listening to requests
	 */
	private boolean listening;



	/**
	 * The stream from which the messages will be received
	 */
	@NotNull
	private final InputStream source;
	/**
	 * The output stream, where the responses will be written to
	 */
	@Nullable
	private final PrintStream target;
	/**
	 * The charset to read/write messages
	 */
	@NotNull
	private final Charset charset;



	/**
	 * The code that signals that a worker process has been started
	 */
	private static final String PROCESS_ALIVE = "#proc-alive";
	/**
	 * The code that signals that a message stream reach its end and should be processed
	 */
	private static final String MESSAGE_DELIMITER = "#msg-end";
	/**
	 * A pattern to split the input stream whenever a message gets fully sent
	 */
	private static final Pattern MESSAGE_DELIMITER_REGEX =
			Pattern.compile("\\n" + MESSAGE_DELIMITER, Pattern.MULTILINE);



	/**
	 * Prepares a new inter-process communication bridge
	 * To start the communication flow, use the {@code listen(MessageHandler)} method of the returned instance
	 * @param source The origin of the messages, that is, the stream that the other process will write the
	 *                  requests to (e.g. In most cases it will be {@code System.in})
	 * @param target The destination of the messages, that is, the stream that the other process will read the
	 *                  responses from (e.g. In most cases it will be {@code System.out})
	 * @param charset The charset to use (default: UTF-8)
	 */
	public IPCWorker(@NotNull InputStream source, @Nullable PrintStream target, @Nullable Charset charset) {
		super();
		if(source == null)
			throw new IllegalArgumentException("The source stream must not be null");

		this.source = source;
		this.target = target;
		this.charset = (charset == null)
			? Charset.UTF8
			: charset;
	}


	/**
	 * Prepares a new inter-process communication bridge, using UTF-8 as the default charset
	 * To start the communication flow, use the {@code listen(MessageHandler)} method of the returned instance
	 * @param source The origin of the messages, that is, the stream that the other process will write the
	 *                  requests to (e.g. In most cases it will be {@code System.in})
	 * @param target The destination of the messages, that is, the stream that the other process will read the
	 *                  responses from (e.g. In most cases it will be {@code System.out})
	 */
	public IPCWorker(@NotNull InputStream source, @Nullable PrintStream target) {
		this(source, target, Charset.UTF8);
	}



	/**
	 * Processes the request message and builds the response, using the provided message processor implementation
	 * @param input The request message (The complete raw message body without the delimiters)
	 * @param messageProcessor An processor implementation that will process the request and provide a response body,
	 *                            without the delimiters
	 * @return The message response, ready to be sent
	 */
	private String processInput(String input, Processor<String, String> messageProcessor) {
		// *Initializing the response recipient:
		String responseJson;
		
		try {
			// Trying to process the received request message:
			responseJson = messageProcessor.process(input);
		} catch (RequestParseException e) {
			responseJson = "{\"error\":{\"message\":\"Error while parsing the request: " + e.getMessage() + "\"}}";
		} catch (ResponseSerializeException e) {
			responseJson = "{\"error\":{\"message\":\"Error while serializing the response: " + e.getMessage() + "\"}}";
		}

		// *Appending the message delimiter in the response
		responseJson += "\n" + MESSAGE_DELIMITER + "\n";

		// *Returning the response message:
		return responseJson;
	}
	

	/**
	 * Starts the listening for requests from the provided streams.
	 * Calling this method will block the current thread, in order to execute the listening loop.
	 * When a new request is received, the provided handler will also be called synchronously in this same thread.
	 * @param requestHandler A handler implementation that should process the received request and change the response
	 *                          to be sent back to the master process
	 */
	public void listen(@Nullable final MessageHandler requestHandler) throws IOException {
		// *Initializing the processor of the requests that will be received, using the given handler implementation:
		final RequestProcessor requestProcessor = new RequestProcessor(requestHandler);
		// *Initializing the unit that will be responsible to process each raw message:
		final Processor<String, String> messageProcessor = new MessageProcessor(requestProcessor);
		
		// *Setting up the scanner that will wait for messages on the 'source' stream:
		final Scanner scanner = new Scanner(source, charset.getCharsetName());
		// *Using the default delimiter rule, to separate each of the raw messages:
		scanner.useDelimiter(MESSAGE_DELIMITER_REGEX);

		// *Signaling that this worker process is ready to receive requests:
		send(PROCESS_ALIVE);

		// *Signaling that this bridge have started listening to requests:
		listening = true;
		
		// *Starting the listening loop:
		while(listening){

			// *Waiting for new input in the source stream:
			final String inputData = scanner.next();

			// *Processing the received input:
			final String response = processInput(inputData, messageProcessor);

			// *Sending the response:
			send(response);
		}
	}


	private byte[] send(String text) throws IOException {
		final byte[] rawText = convertToCharset(text, charset);
		target.write(rawText);
		return rawText;
	}


	@NotNull
	private byte[] convertToCharset(String text, Charset charset){
		if(text == null)
			return new byte[]{};

		try{
			return text.getBytes(java.nio.charset.Charset.forName(charset.getCharsetName()));
		} catch(IllegalCharsetNameException | UnsupportedCharsetException | NullPointerException e){
			return new byte[]{};
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

	@NotNull
	public InputStream getSource() {
		return source;
	}

	@Nullable
	public PrintStream getTarget() {
		return target;
	}



	public static enum Charset{
		UTF8("UTF-8");

		private String charsetName;

		Charset(String charsetName) {
			this.charsetName = charsetName;
		}

		public String getCharsetName() {
			return charsetName;
		}
	}
}
