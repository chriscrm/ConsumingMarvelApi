package com.niu.marvel.api.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Params {
	private long ts;
	private String apikey;
	private String hash;
	private int limit;
}
