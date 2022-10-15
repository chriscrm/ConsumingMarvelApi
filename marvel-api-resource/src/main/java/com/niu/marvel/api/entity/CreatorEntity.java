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
@Table(name = "creator")
public class CreatorEntity implements Serializable {
	
	private static final long serialVersionUID = 3664151277392844686L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long creatorId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String suffix;
	private String fullName;
	private String image;
	@Column(name = "resource_uri")
	private String resourceURI;

	@JsonIgnore
	@ManyToMany(mappedBy = "creators")
	private Collection<ComicEntity> comics;

}
