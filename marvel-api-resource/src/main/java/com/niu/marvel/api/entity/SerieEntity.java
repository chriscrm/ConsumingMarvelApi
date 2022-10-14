package com.niu.marvel.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "serie")
public class SerieEntity implements Serializable {
	
	private static final long serialVersionUID = -6857951470192218659L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long serieId;
	private String title;
	@Column(columnDefinition="TEXT")
	private String description;
	@Column(name = "resource_uri")
	private String resourceURI;
	private int startYear;
	private int endYear;
	private String type;
	private String image;
	
	@ManyToOne()
	@JoinColumn(name = "creator_id", nullable = false)
	private CreatorEntity creator;

}
