package com.niu.marvel.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "comic")
public class ComicEntity implements Serializable {
	
	private static final long serialVersionUID = 635413044747683895L;
	
	@Id
	private long id;
	private String title;
	private int issueNumber;
	@Column(columnDefinition="TEXT")
	private String description;
	private String upc;
	private int pageCount;
	@Column(name = "resource_uri")
	private String resourceURI;
	private String image;
	
}
