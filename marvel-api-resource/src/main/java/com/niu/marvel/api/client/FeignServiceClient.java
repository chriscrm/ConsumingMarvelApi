package com.niu.marvel.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.niu.marvel.api.client.configuration.FeignClientConfig;
import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.CharacterDTO;
import com.niu.marvel.api.dto.ComicDTO;
import com.niu.marvel.api.dto.CreatorDTO;
import com.niu.marvel.api.dto.SerieDTO;

@FeignClient(name = "feignClientMarvel", url = "${external.api.base-url}", configuration = FeignClientConfig.class)
public interface FeignServiceClient {
	
	@GetMapping(value = "/characters", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseModel<CharacterDTO> getCharacters(@SpringQueryMap Params params);
	
	@GetMapping(value = "/comics", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseModel<ComicDTO> getComics(@SpringQueryMap Params params);
	
	@GetMapping(value = "/series", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseModel<SerieDTO> getSeries(@SpringQueryMap Params params);
	
	@GetMapping(value = "/creators", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseModel<CreatorDTO> getCreators(@SpringQueryMap Params params);
	
}
