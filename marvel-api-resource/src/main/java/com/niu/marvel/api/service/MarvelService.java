package com.niu.marvel.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	@SuppressWarnings("unused")
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

		Set<CreatorEntity> savedCreators = new HashSet<>(); // CREATORS LISTS
		Set<SerieEntity> savedSeries = new HashSet<>();
		Set<CharacterEntity> savedCharacters = new HashSet<>();

		// COMIC ITERATION SECTION :::::::::::::::::::::::::::::::::::::::::::::::::
		for (ComicDTO comicItem : comics) {
			CreatorEntity creatorEntityTemp = null;

			// Fetching Creators by comicId
			ResponseModel<CreatorDTO> creatorsResponse = creatorClient
					.getCreatorsByComicId(String.valueOf(comicItem.getId()), params);

			// Fetching Characters by comicId
			ResponseModel<CharacterDTO> charactersResponse = characterClient
					.getCharactersByComicId(String.valueOf(comicItem.getId()), params);

			// CHARACTER ITERATION SECTION :::::::::::::::::::::::::::::::::::::::::::::::::
			for (CharacterDTO characterItem : charactersResponse.getData().getResults()) {
				CharacterEntity characterEntity = modelMapper.map(characterItem, CharacterEntity.class);
				characterEntity.setImage(characterItem.getThumbnail().getPath());
				characterEntity.setCharacterId(characterItem.getId());
				characterEntity.setComics(null);
				characterEntity.setId(0);

				// Persisting Character Entity
				CharacterEntity characterSaved = characterRepository.save(characterEntity);
				savedCharacters.add(characterSaved);
			}

			// CREATOR ITERATION SECTION :::::::::::::::::::::::::::::::::::::::::::::::::
			for (CreatorDTO creatorItem : creatorsResponse.getData().getResults()) {
				creatorEntityTemp = modelMapper.map(creatorItem, CreatorEntity.class);
				creatorEntityTemp.setImage(creatorItem.getThumbnail().getPath());
				creatorEntityTemp.setCreatorId(creatorItem.getId());
				creatorEntityTemp.setId(0);

				// Persisting Creator Entity
				CreatorEntity savedCreator = creatorRepository.save(creatorEntityTemp);
				savedCreators.add(savedCreator);

				// Fetching Series by creatorId
				ResponseModel<SerieDTO> seriesResponse = serieClient
						.getSeriesByCreatorId(String.valueOf(creatorEntityTemp.getId()), params);

				// SERIE ITERATION SECTION :::::::::::::::::::::::::::::::::::::::::::::::::
				for (SerieDTO serieItem : seriesResponse.getData().getResults()) {
					SerieEntity serieEntity = modelMapper.map(serieItem, SerieEntity.class);
					serieEntity.setImage(serieItem.getThumbnail().getPath());
					serieEntity.setSerieId(serieItem.getId());
					serieEntity.setCharacters(savedCharacters);
					serieEntity.setId(0);

					// Persisting Serie entity
					SerieEntity serieSaved = serieRepository.save(serieEntity);
					savedSeries.add(serieSaved);
				}
			}

			// Mapping ComicDTO to ComicEntity
			ComicEntity comicEntity = modelMapper.map(comicItem, ComicEntity.class);
			comicEntity.setImage(comicItem.getThumbnail().getPath());
			comicEntity.setComicId(comicItem.getId());
			comicEntity.setCreators(savedCreators);
			comicEntity.setCharacters(savedCharacters);
			comicEntity.setId(0);

			// Persisting Comic Entity
			comicRepository.save(comicEntity);

		}

		return null;
		// ******************************
	}

}
