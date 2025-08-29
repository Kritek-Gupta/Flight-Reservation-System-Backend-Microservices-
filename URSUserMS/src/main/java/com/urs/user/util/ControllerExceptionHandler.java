package com.urs.user.util;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.urs.user.exception.GlobalExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@Autowired
	Environment env;

	@ExceptionHandler(GlobalExceptionHandler.class)
	public ResponseEntity<Error> exceptionHandler1(GlobalExceptionHandler exception) {
		Error errmsg = new Error();
		errmsg.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errmsg.setErrorMessage(env.getProperty(exception.getMessage()));
		errmsg.setErrorTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(errmsg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Error> exceptionHandler2(MethodArgumentNotValidException exception) {
		Error errmsg = new Error();
		errmsg.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errmsg.setErrorMessage(exception.getBindingResult().getAllErrors().stream().map(ex -> ex.getDefaultMessage())
				.collect(Collectors.joining(", ")));
		errmsg.setErrorTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(errmsg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Error> exceptionHandler3(MethodArgumentTypeMismatchException exception) {
		Error errmsg = new Error();
		errmsg.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errmsg.setErrorMessage(exception.getMessage());
		errmsg.setErrorTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(errmsg, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Error> exceptionHandler4(ConstraintViolationException exception) {
		Error errmsg = new Error();
		errmsg.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errmsg.setErrorMessage(exception.getMessage());
		errmsg.setErrorTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(errmsg, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Error> exceptionHandler4(Exception exception) {
		Error errmsg = new Error();
		errmsg.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errmsg.setErrorMessage(env.getProperty(exception.getMessage()));
		errmsg.setErrorTimeStamp(LocalDateTime.now());
		return new ResponseEntity<>(errmsg, HttpStatus.BAD_REQUEST);
	}
}
