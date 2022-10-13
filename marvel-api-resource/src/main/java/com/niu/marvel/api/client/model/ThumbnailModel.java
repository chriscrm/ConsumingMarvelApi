package com.niu.marvel.api.client.model;

import lombok.Data;

/**
 * The {@code ThumbnailModel} class represents POJO for storage 
 * url path of image as string.
 * 
 * @author crismonterrosa
 *
 */

@Data
public class ThumbnailModel {
	
	/**
	 * The value is used for storage the url image path, represented as a string for convenience
	 */
	private String path;
}
