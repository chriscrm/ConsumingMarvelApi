package com.niu.marvel.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.niu.marvel.api.client.configuration.FeignClientConfig;
import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.SerieDTO;

@FeignClient(name = "feignSerieClient", url = "${external.api.base-url}", configuration = FeignClientConfig.class)
public interface FeignSerieClient {

	@GetMapping(value = "/series/{seriesId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseModel<SerieDTO> getSerieById(@PathVariable String seriesId, @SpringQueryMap Params params);
	
}
