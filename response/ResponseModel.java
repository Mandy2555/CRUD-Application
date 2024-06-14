package com.myapp.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseModel {

	private GenericResponse response;
	private List<Object> info;
	private Object details;

	public ResponseModel() {
		// TODO Auto-generated constructor stub
		super();
	}
	public ResponseModel(GenericResponse response, List<Object> info, Object details) {
		super();
		this.response = response;
		this.info = info;
		this.details = details;
	}

	public GenericResponse getResponse() {
		return response;
	}

	public void setResponse(GenericResponse response) {
		this.response = response;
	}

	public List<Object> getInfo() {
		return info;
	}

	public void setInfo(List<Object> info) {
		this.info = info;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}
}
