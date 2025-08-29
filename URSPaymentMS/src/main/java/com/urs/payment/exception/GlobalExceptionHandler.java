package com.urs.payment.exception;

public class GlobalExceptionHandler extends Exception {

	private static final long serialVersionUID = 1L;

	public GlobalExceptionHandler(String message) {
		super(message);
	}
}
