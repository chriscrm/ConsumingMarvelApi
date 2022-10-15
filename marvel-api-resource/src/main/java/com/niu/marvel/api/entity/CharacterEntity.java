package com.niu.marvel.api.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "characters")
public class CharacterEntity implements Serializable {

	private static final long serialVersionUID = -2612022351283982533L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long characterId;
	private String name;
	@Column(columnDefinition="TEXT")
	private String description;
	private String image;

	@JsonIgnore
	@ManyToMany(mappedBy = "characters")
	private Collection<ComicEntity> comics;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "characters")
	private Collection<SerieEntity> series;
}
