package com.niu.marvel.api.dto;

import com.niu.marvel.api.client.model.ThumbnailModel;

import lombok.Data;

@Data
public class ComicDTO {
	private long id;
	private String title;
	private int issueNumber;
	private String description;
	private String upc;
	private int pageCount;
	private String resourceURI;
	private SerieDTO series;
	private ThumbnailModel thumbnail;
}

