package br.com.gft.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.gft.domain.Anime;
import br.com.gft.repository.AnimeRepository;
import br.com.gft.util.Utils;

@Repository
public class AnimeService {
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private AnimeRepository animeRepository;

	public Page<Anime> listAll(Pageable page) {
		return animeRepository.findAll(page);
	}
	
	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}
	
	public Anime findById(int id) {
		return utils.findAnimeOtThrowNotFound(id, animeRepository);
	}
	
	@Transactional
	public Anime save(Anime anime) {
		return animeRepository.save(anime);
	}

	public void delete(int id) {
		animeRepository.delete(utils.findAnimeOtThrowNotFound(id, animeRepository));		
	}

	public void update(Anime anime) {		
		animeRepository.save(anime);
	}

}
