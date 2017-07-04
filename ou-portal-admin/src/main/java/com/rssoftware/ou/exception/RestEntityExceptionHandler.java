package com.rssoftware.ou.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAll(Exception e, WebRequest request){
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "Internal Error Occurred.");
		return(new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()));
	}
}
