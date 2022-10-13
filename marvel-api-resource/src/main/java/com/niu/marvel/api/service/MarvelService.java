package com.niu.marvel.api.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.niu.marvel.api.client.FeignSerieClient;
import com.niu.marvel.api.client.FeignServiceClient;
import com.niu.marvel.api.client.Params;
import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.CharacterDTO;
import com.niu.marvel.api.dto.ComicDTO;
import com.niu.marvel.api.dto.CreatorDTO;
import com.niu.marvel.api.dto.SerieDTO;
import com.niu.marvel.api.entity.ComicEntity;
import com.niu.marvel.api.repository.ComicRepository;
import com.niu.marvel.api.utils.Utils;

@Service
public class MarvelService {

	private final FeignServiceClient client;
	private final FeignSerieClient serieClient;
	private final Params params;
	private final Environment env;
	
	private final ComicRepository comicRepository;
	private final ModelMapper modelMapper;
	
	/**
	 * MarvelService constructor with required parameters
	 * @param client FeignClient Service
	 * @param env Environment variable for accessing application properties values
	 */
	public MarvelService(FeignServiceClient client, 
			Environment env, 
			ModelMapper mapper, 
			ComicRepository comicRepository, FeignSerieClient serieClient) {
		this.client = client;
		this.serieClient = serieClient;
		this.env = env;
		this.params = new Params(Integer.parseInt(env.getProperty("external.api.ts")),
				env.getProperty("external.api.apikey"), 
				env.getProperty("external.api.hash"),
				Integer.parseInt(env.getProperty("external.api.limit")));
		this.comicRepository = comicRepository;
		this.modelMapper = mapper;
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
	 * Persist all data from comics to Comic Entity using ComicRepository
	 * @return returns a list of comics using given parameters of the 
	 * server-side application of Marvel Api Authentication
	 */
	public ResponseModel<ComicDTO> getComics(){
		
		ResponseModel<ComicDTO> result = client.getComics(params);
		
		List<ComicDTO> comics = (List<ComicDTO>) result.getData().getResults();
		List<SerieDTO> series = new ArrayList<>();
		
		List<ComicEntity> comicEntities = new ArrayList<>();
		
		comics.forEach(comic -> {
			ComicEntity comicEntity = modelMapper.map(comic, ComicEntity.class);
			comicEntity.setImage(comic.getThumbnail().getPath());
			comicEntities.add(comicEntity);

			String serieId = Utils.getIdByResource(comic.getSeries().getResourceURI());
			ResponseModel<SerieDTO> responseSerie = serieClient.getSerieById(serieId, params);
			
			responseSerie.getData().getResults().forEach(serie -> {
				series.add(serie);
			});
			
		});

		//saving all comics entities
		comicRepository.saveAll(comicEntities);

		return result;
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
