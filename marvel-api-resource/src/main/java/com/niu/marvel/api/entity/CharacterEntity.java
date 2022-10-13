package com.niu.marvel.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "characters")
public class CharacterEntity {

	@Id
	private long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String thumbnail;
	
	
	
}
