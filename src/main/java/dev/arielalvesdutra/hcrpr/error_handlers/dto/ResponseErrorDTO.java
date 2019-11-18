package dev.arielalvesdutra.hcrpr.error_handlers.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResponseErrorDTO {
	
	private final String error;
	private final String message;
	private final Integer status;
	private final String path;
	private final Instant timestamp;
	private final List<?> errors; 
	
	public ResponseErrorDTO(
			String error, String message, Integer status, 
			Instant timestamp, String path) {
		
		this.error = error;
		this.message = message;
		this.path = path;
		this.status = status;
		this.timestamp = timestamp;
		this.errors = new ArrayList<>();
	}
	
    public ResponseErrorDTO(Integer status, Map<String, Object> errorAttributes) {
    	
        this.status = status;
    	this.error = (String) errorAttributes.get("error");
		this.message = (String) errorAttributes.get("message");
		this.path = (String) errorAttributes.get("path");
		this.errors = (List<?>) errorAttributes.get("errors");
		
		Date dateTimestamp = (Date) errorAttributes.get("timestamp");	
		this.timestamp = dateTimestamp.toInstant();	
    }
	
	public String getError() {
		return this.error;
	}

	public String getMessage() {
		return message;
	}
	
	public String getPath() {
		return this.path;
	}

	public Integer getStatus() {
		return status;
	}		
	
	public Instant getTimestamp() {
		return this.timestamp;
	}
	
	public List<?> getErrors() {
		return this.errors;
	}
}
