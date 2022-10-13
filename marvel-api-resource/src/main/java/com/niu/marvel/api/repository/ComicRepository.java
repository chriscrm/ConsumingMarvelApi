package com.niu.marvel.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niu.marvel.api.entity.ComicEntity;

@Repository
public interface ComicRepository extends CrudRepository<ComicEntity, Long> {

}
