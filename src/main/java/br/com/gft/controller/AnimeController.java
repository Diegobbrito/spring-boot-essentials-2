package br.com.gft.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gft.domain.Anime;
import br.com.gft.service.AnimeService;

@RestController
@RequestMapping("animes")
public class AnimeController {

	@Autowired
	private AnimeService animeService;
	
	@GetMapping()
	public ResponseEntity<Page<Anime>> listAll(Pageable pageable) {
		return ResponseEntity.ok(animeService.listAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Anime> findById(@PathVariable int id) {
		return ResponseEntity.ok(animeService.findById(id));
	}
	
	@GetMapping("/find")
	public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name") String name) {
		return ResponseEntity.ok(animeService.findByName(name));
	}
	
	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody @Valid Anime anime){
		animeService.save(anime);
		return ResponseEntity.ok(animeService.save(anime));	
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody Anime anime){
		animeService.update(anime);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id){
		animeService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}

}
