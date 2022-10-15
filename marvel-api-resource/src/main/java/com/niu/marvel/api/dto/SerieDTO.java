package com.niu.marvel.api.dto;

import com.niu.marvel.api.client.model.ThumbnailModel;

import lombok.Data;

@Data
public class SerieDTO {
	private long id;
	private String title;
	private String description;
	private String resourceURI;
	private int startYear;
	private int endYear;
	private String type;
	private ThumbnailModel thumbnail;
	
}
