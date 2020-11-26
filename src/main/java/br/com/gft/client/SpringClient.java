package br.com.gft.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.gft.domain.Anime;

public class SpringClient {
	public static void main(String[] args) {
		
		ResponseEntity<Anime> animeResponseEntity = 
				new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 1);
		
		Anime anime = 
				new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);
		
		Anime[] animeArray = 
				new RestTemplate().getForObject("http://localhost:8080/animes", Anime[].class);
		
		ResponseEntity<List<Anime>> exchangeAnimeList = new RestTemplate()
			.exchange("http://localhost:8080/animes",
					HttpMethod.GET, null, 
					new ParameterizedTypeReference<List<Anime>>() {}
			);
	}
}
