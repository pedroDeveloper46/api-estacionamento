package com.pedrodev.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {
	
	private int status;
	private String error;
	private String message;
	private LocalDateTime time;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public ErrorResponse(int status, String error, String message, LocalDateTime time) {
	
		this.status = status;
		this.error = error;
		this.message = message;
		this.time = time;
	}
	
	

}
