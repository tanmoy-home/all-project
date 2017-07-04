/**
 * 
 */
package com.rssoftware.ou.exception;



/**
 * @author MalobikaM
 *
 */
public class TokenisationException extends RuntimeException{
	private String code;
	private String message;
	public TokenisationException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
}
