package com.niu.marvel.api.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "comic")
public class ComicEntity implements Serializable {
	
	private static final long serialVersionUID = 635413044747683895L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long comicId;
	private String title;
	private int issueNumber;
	@Column(columnDefinition="TEXT")
	private String description;
	private String upc;
	private int pageCount;
	@Column(name = "resource_uri")
	private String resourceURI;
	private String image;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "comic_has_creators", joinColumns = @JoinColumn(name = "comic_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "creator_id", referencedColumnName = "id"))
	private Collection<CreatorEntity> creators;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "comics_has_characters", joinColumns = @JoinColumn(name = "comic_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"))
	private Collection<CharacterEntity> characters;
	
}
