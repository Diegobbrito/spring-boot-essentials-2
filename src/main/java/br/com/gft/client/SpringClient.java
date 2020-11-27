package br.com.gft.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.gft.domain.Anime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringClient {
	public static void main(String[] args) {

//		testGetWithRestTemplate();

		ResponseEntity<List<Anime>> exchangeAnimeList = new RestTemplate().exchange("http://localhost:8080/animes",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
				});

		Anime overload = Anime.builder().name("Overload").build();

		Anime overloadSaved = new RestTemplate().postForObject("http://localhost:8080/animes", overload, Anime.class);

		Anime steinsGate = Anime.builder().name("Steins Gate").build();

		Anime steinsGateSaved = new RestTemplate()
				.exchange("http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>(steinsGate, createJsonHeader()), Anime.class ).getBody();

		steinsGate.setName("Steins Gate Zero");

		ResponseEntity<Void> exchangeUpdated = new RestTemplate()
				.exchange("http://localhost:8080/animes", HttpMethod.PUT, new HttpEntity<>(steinsGate, createJsonHeader()), Void.class );

		ResponseEntity<Void> exchangeDeleted = new RestTemplate()
				.exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE, 
						null, Void.class, steinsGateSaved.getId());

	}

	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

	private static void testGetWithRestTemplate() {
		ResponseEntity<Anime> animeResponseEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}",
				Anime.class, 1);

		Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);

		Anime[] animeArray = new RestTemplate().getForObject("http://localhost:8080/animes", Anime[].class);
	}
}
