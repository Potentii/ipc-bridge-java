package com.potentii.ipc.worker.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class OutgoingMessage implements Serializable{

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
	private Object data;



	/* ===========================
	 * Constructors
	 * ===========================
	 */

	public OutgoingMessage(@NotNull final String id) {
		super();
		if(id == null)
			throw new IllegalArgumentException("The message id must not be null");
		this.id = id;
		this.query = new HashMap<>();
	}



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
	 * Sets a query parameter value
	 * @param key The parameter name
	 * @param value The new parameter value
	 */
	public void query(@NotNull final String key, @Nullable final Object value) {
		query.put(key, value);
	}


	/**
	 * Sets an error for this message
	 *  Only set an error if this message may be signaled as failed
	 * @param error The new error of this message
	 */
	public void error(@Nullable final Throwable error) {
		this.error = error;
	}


	/**
	 * Sets the raw content of the message
	 * @param content The content data of this message
	 */
	public void text(@Nullable final String content) {
		data = content;
	}


	/**
	 * Serializes and sets the content of the message as an object
	 * @param obj The object that should be the content
	 * @param <T> The type of the object
	 * @throws ResponseSerializeException Whether an error occur when trying to serialize the new content
	 */
	public <T> void json(@Nullable final T obj) throws ResponseSerializeException {
		try {
			// *Serializing the given object as a JSON string if possible:
			final String data = (obj == null)
                    ? null
                    : new ObjectMapper().writeValueAsString(obj);

			// *Writing the serialized string into the content:
			text(data);
		} catch (JsonProcessingException e) {
			throw new ResponseSerializeException(e);
		}
	}



	/* ===========================
	 * Getters and setters
	 * ===========================
	 */

	public String getId() {
		return id;
	}

	public Map<String, Object> getQuery() {
		return query;
	}

	public Throwable getError() {
		return error;
	}

	public Object getData() {
		return data;
	}

}
