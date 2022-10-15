package com.niu.marvel.api.dto;

import com.niu.marvel.api.client.model.ThumbnailModel;

import lombok.Data;

@Data
public class CreatorDTO {
	private long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String suffix;
	private String fullName;
	private ThumbnailModel thumbnail;
	private String resourceURI;
	
}
