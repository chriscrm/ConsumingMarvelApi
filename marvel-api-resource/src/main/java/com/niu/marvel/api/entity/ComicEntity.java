package com.niu.marvel.api.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CreatorEntity creator;
	
//	@ManyToOne(fetch = FetchType.LAZY)
////	@JoinColumn(name = "creator_id", nullable = false)
//	private CreatorEntity creator;
//	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "comics_has_characters", joinColumns = @JoinColumn(name = "comic_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"))
	@JsonBackReference
	private Collection<CharacterEntity> characters;
//	
//	@ManyToOne
//	private SerieEntity serie;
	
}
