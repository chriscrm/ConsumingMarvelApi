package com.niu.marvel.api.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.niu.marvel.api.client.FeignServiceClient;
import com.niu.marvel.api.client.Params;
import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.CharacterDTO;
import com.niu.marvel.api.dto.ComicDTO;
import com.niu.marvel.api.dto.CreatorDTO;
import com.niu.marvel.api.dto.SerieDTO;

@Service
public class MarvelService {

	private final FeignServiceClient client;
	private final Params params;
	private final Environment env;
	
	
	/**
	 * MarvelService constructor with required parameters
	 * @param client FeignClient Service
	 * @param env Environment variable for accessing application properties values
	 */
	public MarvelService(FeignServiceClient client, Environment env) {
		this.client = client;
		this.env = env;
		this.params = new Params(Integer.parseInt(env.getProperty("external.api.ts")),
				env.getProperty("external.api.apikey"), 
				env.getProperty("external.api.hash"));
	}

	/**
	 * Method getCharacters()
	 * @return returns a list of characters using given parameters of the 
	 * server-side application of Marvel Api Authentication
	 */
	public ResponseModel<CharacterDTO> getCharacters(){
		return client.getCharacters(params);
	};
	
	/**
	 * Method getComics()
	 * @return returns a list of comics using given parameters of the 
	 * server-side application of Marvel Api Authentication
	 */
	public ResponseModel<ComicDTO> getComics(){
		return client.getComics(params);
	}

	/**
	 * Method getSeries()
	 * @return returns a list of series using given parameters of the  
	 * server-side application of Marvel Api Authentication
	 */
	public ResponseModel<SerieDTO> getSeries(){
		return client.getSeries(params);
	}
	
	/**
	 * Method getCreators()
	 * @return returns a list of creators using given parameters of the  
	 * server-side application of Marvel Api Authentication
	 */
	public ResponseModel<CreatorDTO> getCreators(){
		return client.getCreators(params);
	}
	
}
