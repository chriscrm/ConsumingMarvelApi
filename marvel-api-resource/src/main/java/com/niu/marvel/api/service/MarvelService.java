package com.niu.marvel.api.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.niu.marvel.api.client.FeignCharacterClient;
import com.niu.marvel.api.client.FeignCreatorClient;
import com.niu.marvel.api.client.FeignSerieClient;
import com.niu.marvel.api.client.FeignServiceClient;
import com.niu.marvel.api.client.Params;
import com.niu.marvel.api.client.model.ResponseModel;
import com.niu.marvel.api.dto.CharacterDTO;
import com.niu.marvel.api.dto.ComicDTO;
import com.niu.marvel.api.dto.CreatorDTO;
import com.niu.marvel.api.dto.SerieDTO;
import com.niu.marvel.api.entity.CharacterEntity;
import com.niu.marvel.api.entity.ComicEntity;
import com.niu.marvel.api.entity.CreatorEntity;
import com.niu.marvel.api.entity.SerieEntity;
import com.niu.marvel.api.repository.CharacterRepository;
import com.niu.marvel.api.repository.ComicRepository;
import com.niu.marvel.api.repository.CreatorRepository;
import com.niu.marvel.api.repository.SerieRepository;

@Service
public class MarvelService {

	private final FeignServiceClient client;
	private final FeignSerieClient serieClient;
	private final FeignCreatorClient creatorClient;
	private final FeignCharacterClient characterClient;
	private final Params params;
	private final Environment env;
	
	private final ComicRepository comicRepository;
	private final CreatorRepository creatorRepository;
	private final SerieRepository serieRepository;
	private final CharacterRepository characterRepository;
	private final ModelMapper modelMapper;
	
	/**
	 * MarvelService constructor with required parameters
	 * @param client FeignClient Service
	 * @param env Environment variable for accessing application properties values
	 */
	public MarvelService(FeignServiceClient client, 
			Environment env, 
			ModelMapper mapper, 
			ComicRepository comicRepository, FeignSerieClient serieClient, FeignCreatorClient creatorClient, CreatorRepository creatorRepository, SerieRepository serieRepository, FeignCharacterClient characterClient, CharacterRepository characterRepository) {
		this.client = client;
		this.serieClient = serieClient;
		this.creatorClient = creatorClient;
		this.characterClient = characterClient;
		this.env = env;
		this.params = new Params(Integer.parseInt(env.getProperty("external.api.ts")),
				env.getProperty("external.api.apikey"), 
				env.getProperty("external.api.hash"),
				Integer.parseInt(env.getProperty("external.api.limit")));
		this.comicRepository = comicRepository;
		this.creatorRepository = creatorRepository;
		this.serieRepository = serieRepository;
		this.characterRepository = characterRepository;
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
		
//		ResponseModel<ComicDTO> result = client.getComics(params);
//		
//		List<ComicDTO> comics = (List<ComicDTO>) result.getData().getResults();
//		List<SerieDTO> series = new ArrayList<>();
//		
//		List<ComicEntity> comicEntities = new ArrayList<>();
//		
//		comics.forEach(comic -> {
//			ComicEntity comicEntity = modelMapper.map(comic, ComicEntity.class);
//			comicEntity.setImage(comic.getThumbnail().getPath());
//			comicEntities.add(comicEntity);
//
//			String serieId = Utils.getIdByResource(comic.getSeries().getResourceURI());
//			ResponseModel<SerieDTO> responseSerie = serieClient.getSerieById(serieId, params);
//			
//			responseSerie.getData().getResults().forEach(serie -> {
//				series.add(serie);
//			});
//			
//		});
//
//		//saving all comics entities
//		comicRepository.saveAll(comicEntities);

		return null;
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
		
		ResponseModel<CreatorDTO> results = client.getCreators(params);
		
		List<CreatorDTO> creators = (List<CreatorDTO>) results.getData().getResults();
		
		// Iterating creators and saving entity relation
		creators.forEach(creator -> {
			CreatorEntity creatorEntity = modelMapper.map(creator, CreatorEntity.class);
			creatorEntity.setImage(creator.getThumbnail().getPath());
			creatorEntity.setCreatorId(creator.getId());
			
			CreatorEntity savedCreator = creatorRepository.save(creatorEntity);
			
			// Getting creatorId of current creator iteration
			ResponseModel<ComicDTO> comicsResponse = creatorClient.getComicsByCreatorId(String.valueOf(creator.getId()), params);
			
			// Iterating comics that belongs to the current Creator
			comicsResponse.getData().getResults().forEach(comic -> {
				ComicEntity comicEntity = modelMapper.map(comic, ComicEntity.class);
				comicEntity.setImage(comic.getThumbnail().getPath());
				comicEntity.setComicId(comic.getId());
				comicEntity.setCreator(savedCreator);
				
				// Fetching Characters by comicId
				ResponseModel<CharacterDTO> charactersResponse = 
						characterClient.getCharactersByComicId(String.valueOf(comic.getId()), params);
				
				List<CharacterEntity> characters = new ArrayList<>();
				
				charactersResponse.getData().getResults().forEach(character -> {
					CharacterEntity characterEntity = modelMapper.map(character, CharacterEntity.class);
					characterEntity.setImage(character.getThumbnail().getPath());
					characterEntity.setCharacterId(character.getId());
					
					CharacterEntity savedCharacter = characterRepository.save(characterEntity);
					characters.add(savedCharacter);
				});
				
				comicEntity.setCharacters(characters);
				
				comicRepository.save(comicEntity);
				
			});
			
			// Getting Series Response Model by creatorId
			ResponseModel<SerieDTO> seriesResponse = serieClient.getSeriesByCreatorId(String.valueOf(creator.getId()), params);

			seriesResponse.getData().getResults().forEach(serie -> {
				SerieEntity serieEntity = modelMapper.map(serie, SerieEntity.class);
				serieEntity.setImage(serie.getThumbnail().getPath());
				serieEntity.setSerieId(serie.getId());
				serieEntity.setCreator(savedCreator);
				
				serieRepository.save(serieEntity);
			});
			
		});
		
		return results;
	}
	
}
