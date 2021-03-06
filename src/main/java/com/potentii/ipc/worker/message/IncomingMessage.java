package com.potentii.ipc.worker.message;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class IncomingMessage implements Serializable{

	/* ===========================
	 * Fields
	 * ===========================
	 */
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String id;
	@Nullable
	private Map<String, Object> query;
	@Nullable
	private Throwable error;
	@Nullable
	private String data;



	/* ===========================
	 * Public API methods
	 * ===========================
	 */

	/**
	 * Retrieves this message's identifier
	 * @return The message identifier
	 */
	public String id() {
		return id;
	}


	/**
	 * Retrieves the desired query parameter
	 * @param key The parameter name
	 * @return The parameter value, or null if it doesn't exist
	 */
	public Object query(@NotNull final String key) {
		return (query == null)
				? null
				: query.get(key);
	}


	/**
	 * Retrieves the desired query parameter as a string
	 * @param key The parameter name
	 * @return The parameter value, or null if it doesn't exist
	 */
	public String queryString(@NotNull final String key) {
		// *Getting the query parameter as object first:
		final Object value = query(key);

		// *Converting it into a string if possible:
		return (value == null)
				? null
				: String.valueOf(value);
	}


	/**
	 * Reads the raw message content
	 * @return The message content
	 */
	public String text() {
		return data;
	}


	/**
	 * Parses and retrieves the content data as an object
	 * @param clazz The class that the content should be parsed to
	 * @param <T> The type of the returned object
	 * @return The parsed object, or null if it's not set
	 * @throws RequestParseException Whether an error occur during the parse of the content
	 */
	public <T> T json(@NotNull final Class<T> clazz) throws RequestParseException {
		try {
			// *Parsing the content as an object, if it's possible:
			return (text() == null)
					? null 
					: new ObjectMapper().readValue(text(), clazz);
		} catch (IOException e) {
			throw new RequestParseException(e);
		}
	}



	/* ===========================
	 * Getters and setters
	 * ===========================
	 */

	public void setId(String id) {
		this.id = id;
	}

	public void setQuery(Map<String, Object> query) {
		this.query = query;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public void setData(String data) {
		this.data = data;
	}

}
