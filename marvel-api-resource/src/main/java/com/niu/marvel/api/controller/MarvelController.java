package com.niu.marvel.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.ComicDTO;
import com.niu.marvel.api.entity.CharacterEntity;
import com.niu.marvel.api.service.MarvelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marvel")
@RequiredArgsConstructor
public class MarvelController {

	private final MarvelService characterService;

	@Operation(summary = "Fetch all Data from Marvel API")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "OK", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = CharacterEntity.class)) }), 
		  @ApiResponse(responseCode = "404", description = "data not found", 
		    content = @Content),
		  @ApiResponse(responseCode = "409", description = "Limit greater than 100", 
		    content = @Content) 
			  })
	@GetMapping("/fetch-all-data")
	public ResponseEntity<ResponseModel<ComicDTO>> getAllComics(@RequestParam(value = "limit", defaultValue = "100") int limit){
		return new ResponseEntity<>(characterService.getComics(),HttpStatus.OK);
	}

}
