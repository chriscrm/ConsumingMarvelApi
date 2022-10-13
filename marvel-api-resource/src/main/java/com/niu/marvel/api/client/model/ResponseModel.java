package com.niu.marvel.api.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseModel<T> {
	
	@JsonIgnore
	T t;
	private final String status;
	@JsonProperty("data")
	private final DataResponseModel<T> data;
	
	public ResponseModel(T t, String status, DataResponseModel<T> data) {
		this.t = t;
		this.status = status;
		this.data = data;
	}
	
	
}
