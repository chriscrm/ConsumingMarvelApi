package com.niu.marvel.api.dto;

import com.niu.marvel.api.client.model.ThumbnailModel;

import lombok.Data;

@Data
public class CharacterDTO {
	private long id;
	private String name;
	private String description;
	private ThumbnailModel thumbnail;

}
