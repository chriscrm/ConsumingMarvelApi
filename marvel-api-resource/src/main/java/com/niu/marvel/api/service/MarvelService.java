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
	
	public MarvelService(FeignServiceClient client, Environment env) {
		this.client = client;
		this.env = env;
		this.params = new Params(Integer.parseInt(env.getProperty("external.api.ts")),
				env.getProperty("external.api.apikey"), 
				env.getProperty("external.api.hash"));
	}

	public ResponseModel<CharacterDTO> getCharacters(){
		return client.getCharacters(params);
	};
	
	public ResponseModel<ComicDTO> getComics(){
		return client.getComics(params);
	}

	public ResponseModel<SerieDTO> getSeries(){
		return client.getSeries(params);
	}
	
	public ResponseModel<CreatorDTO> getCreators(){
		return client.getCreators(params);
	}
	
}
