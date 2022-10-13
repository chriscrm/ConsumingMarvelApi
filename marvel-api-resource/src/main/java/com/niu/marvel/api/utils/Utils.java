package com.niu.marvel.api.utils;

public class Utils {
	
	public static String getIdByResource(String resourceUrl) {
		String[] segments = resourceUrl.split("/");
		return segments[6];
	}

}
