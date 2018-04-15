package com.potentii.ipc.service.message;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;

public class IPCRequest implements Serializable{

	/* ===========================
	 * Fields
	 * ===========================
	 */
	
	private static final long serialVersionUID = 1L;

	private String id;
	private Map<String, Object> query;
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
	 * Reads the raw request content
	 * @return The request content
	 */
	public String text() {
		return data;
	}


	/**
	 * Parses and retrieves the content data as an object
	 * @param clazz The class that the content should be parsed to
	 * @param <T> The type of the returned object
	 * @return The parsed object
	 * @throws RequestParseException Whether an error occur during the parse of the content
	 */
	public <T> T json(@NotNull final Class<T> clazz) throws RequestParseException {
		try {
			// *Re
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

	public void setData(String data) {
		this.data = data;
	}
	
}
