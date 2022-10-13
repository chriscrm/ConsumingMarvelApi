package com.niu.marvel.api.client.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DataResponseModel<T> {
	@JsonProperty("results")
	public Collection<T> results;
}
