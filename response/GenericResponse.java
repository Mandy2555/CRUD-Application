package com.myapp.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GenericResponse {

	private Boolean status;
	private int resultCode;
	private String responseCode;
	private String message;

	public GenericResponse() {
		// TODO Auto-generated constructor stub
	}

	public GenericResponse(Boolean status, int resultCode, String responseCode, String message ) {
		super();
		this.status = status;
		this.resultCode = resultCode;
		this.responseCode = responseCode;
		this.message = message;
	}


	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
