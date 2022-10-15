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
	 * 
	 * @param client FeignClient Service
	 * @param env    Environment variable for accessing application properties
	 *               values
	 */
	public MarvelService(FeignServiceClient client, Environment env, ModelMapper mapper,
			ComicRepository comicRepository, FeignSerieClient serieClient, FeignCreatorClient creatorClient,
			CreatorRepository creatorRepository, SerieRepository serieRepository, FeignCharacterClient characterClient,
			CharacterRepository characterRepository) {
		this.client = client;
		this.serieClient = serieClient;
		this.creatorClient = creatorClient;
		this.characterClient = characterClient;
		this.env = env;
		this.params = new Params(Integer.parseInt(env.getProperty("external.api.ts")),
				env.getProperty("external.api.apikey"), env.getProperty("external.api.hash"),
				Integer.parseInt(env.getProperty("external.api.limit")));
		this.comicRepository = comicRepository;
		this.creatorRepository = creatorRepository;
		this.serieRepository = serieRepository;
		this.characterRepository = characterRepository;
		this.modelMapper = mapper;
	}

	/**
	 * Method getComics() Persist all data from comics to Comic Entity using
	 * ComicRepository
	 * 
	 * @return returns a list of comics using given parameters of the server-side
	 *         application of Marvel Api Authentication
	 */
	public ResponseModel<ComicDTO> getComics() {

		ResponseModel<ComicDTO> results = client.getComics(params);

		List<ComicDTO> comics = (List<ComicDTO>) results.getData().getResults();
		List<SerieDTO> seriesDTOs = new ArrayList<>();
		List<CharacterEntity> characterList = new ArrayList<>();

		for (ComicDTO comicItem : comics) {
			CreatorEntity creatorEntityTemp = null;
			
			// Fetching Creators by comicId
			ResponseModel<CreatorDTO> creatorsResponse =
					creatorClient.getCreatorsByComicId(String.valueOf(comicItem.getId()), params);

			for (CreatorDTO creatorItem : creatorsResponse.getData().getResults()) {
				creatorEntityTemp = modelMapper.map(creatorItem, CreatorEntity.class);
				creatorEntityTemp.setImage(creatorItem.getThumbnail().getPath());
				creatorEntityTemp.setCreatorId(creatorItem.getId());
				creatorEntityTemp.setId(0);

				// Persisting Creator Entity
				creatorRepository.save(creatorEntityTemp);

				// Fetching Series by creatorId
				ResponseModel<SerieDTO> seriesResponse = serieClient.getSeriesByCreatorId(String.valueOf(creatorEntityTemp.getId()), params);

				seriesDTOs = (List<SerieDTO>) seriesResponse.getData().getResults();
				
				for (SerieDTO serieItem : seriesResponse.getData().getResults()) {
					SerieEntity serieEntity = modelMapper.map(serieItem, SerieEntity.class);
					serieEntity.setImage(serieItem.getThumbnail().getPath());
					serieEntity.setSerieId(serieItem.getId());
					serieEntity.setId(0);

					// Persisting Serie entity
					serieRepository.save(serieEntity);
				}
			}
			
			// Fetching Characters by comicId
			ResponseModel<CharacterDTO> charactersResponse = 
					characterClient.getCharactersByComicId(String.valueOf(comicItem.getId()), params);

			for(CharacterDTO characterItem : charactersResponse.getData().getResults()) {
				CharacterEntity characterEntity = modelMapper.map(characterItem, CharacterEntity.class);
				characterEntity.setImage(characterItem.getThumbnail().getPath());
				characterEntity.setCharacterId(characterItem.getId());
				characterEntity.setComics(null);
				characterEntity.setId(0);
				
				// Persisting Character Entity
				CharacterEntity characterSaved = characterRepository.save(characterEntity);
				characterList.add(characterSaved);
			}
			
			// Mapping ComicDTO to ComicEntity
			ComicEntity comicEntity = modelMapper.map(comicItem, ComicEntity.class);
			comicEntity.setImage(comicItem.getThumbnail().getPath());
			comicEntity.setComicId(comicItem.getId());
			comicEntity.setCreator(creatorEntityTemp);
			comicEntity.setCharacters(characterList);
			comicEntity.setId(0);

			// Persisting Comic Entity
			comicRepository.save(comicEntity);

		}
		


		return null;
		// ******************************
	}

	/**
	 * Method getCreators()
	 * 
	 * @return returns a list of creators using given parameters of the server-side
	 *         application of Marvel Api Authentication
	 */
	public ResponseModel<CreatorDTO> getCreators() {

//		ResponseModel<CreatorDTO> results = client.getCreators(params);
//		
//		List<CreatorDTO> creators = (List<CreatorDTO>) results.getData().getResults();
//		
//		// Iterating creators and saving entity relation
//		creators.forEach(creator -> {
//			CreatorEntity creatorEntity = modelMapper.map(creator, CreatorEntity.class);
//			creatorEntity.setImage(creator.getThumbnail().getPath());
//			creatorEntity.setCreatorId(creator.getId());
//			
//			CreatorEntity savedCreator = creatorRepository.save(creatorEntity);
//			
//			// Getting creatorId of current creator iteration
//			ResponseModel<ComicDTO> comicsResponse = creatorClient.getComicsByCreatorId(String.valueOf(creator.getId()), params);
//			
//			// Iterating comics that belongs to the current Creator
//			comicsResponse.getData().getResults().forEach(comic -> {
//				ComicEntity comicEntity = modelMapper.map(comic, ComicEntity.class);
//				comicEntity.setImage(comic.getThumbnail().getPath());
//				comicEntity.setComicId(comic.getId());
//				comicEntity.setCreator(savedCreator);
//				
//				// Fetching Characters by comicId
//				ResponseModel<CharacterDTO> charactersResponse = 
//						characterClient.getCharactersByComicId(String.valueOf(comic.getId()), params);
//				
//				List<CharacterEntity> characters = new ArrayList<>();
//				
//				charactersResponse.getData().getResults().forEach(character -> {
//					CharacterEntity characterEntity = modelMapper.map(character, CharacterEntity.class);
//					characterEntity.setImage(character.getThumbnail().getPath());
//					characterEntity.setCharacterId(character.getId());
//					
//					CharacterEntity savedCharacter = characterRepository.save(characterEntity);
//					characters.add(savedCharacter);
//				});
//				
//				comicEntity.setCharacters(characters);
//				
//				comicRepository.save(comicEntity);
//				
//			});
//			
//			// Getting Series Response Model by creatorId
//			ResponseModel<SerieDTO> seriesResponse = serieClient.getSeriesByCreatorId(String.valueOf(creator.getId()), params);
//
//			seriesResponse.getData().getResults().forEach(serie -> {
//				SerieEntity serieEntity = modelMapper.map(serie, SerieEntity.class);
//				serieEntity.setImage(serie.getThumbnail().getPath());
//				serieEntity.setSerieId(serie.getId());
//				serieEntity.setCreator(savedCreator);
//				
//				serieRepository.save(serieEntity);
//			});
//			
//		});

		return null;
	}

}
