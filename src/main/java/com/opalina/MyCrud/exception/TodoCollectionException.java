package com.opalina.MyCrud.exception;

public class TodoCollectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TodoCollectionException(String message) {
		super(message);
	}
	
	public static String NotFoundException(String id) {
		return "Todo with ID: " + id + ", not found";
	}
	
	public static String TodoAlreadyExists() {
		return "Todo already exists";
	}
}
