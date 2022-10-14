package com.niu.marvel.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niu.marvel.api.entity.SerieEntity;

@Repository
public interface SerieRepository extends CrudRepository<SerieEntity, Long>{

}
