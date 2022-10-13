package com.niu.marvel.api.dto;

import lombok.Data;

@Data
public class ComicDTO {
	private long id;
	private String title;
	private int issueNumber;
	private String description;
	private String upc;
	private String resourceURI;
	
}
