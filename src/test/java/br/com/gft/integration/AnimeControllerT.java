package br.com.gft.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.gft.domain.Anime;
import br.com.gft.repository.AnimeRepository;
import br.com.gft.util.AnimeCreator;
import br.com.gft.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnimeControllerT {

	@Autowired
	private TestRestTemplate restRestTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private AnimeRepository animeRepositoryMock;

	@BeforeEach
	public void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);

		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyInt()))
				.thenReturn(Optional.of(AnimeCreator.createValidAnime()));

		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
		.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeRepositoryMock)
		.delete(ArgumentMatchers.any(Anime.class));
		
		BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidAnime()))
		.thenReturn(AnimeCreator.createValidUpdatedAnime());
	}

	@Test
	@DisplayName("listAll returns a pageable list of animes when successful")
	public void listAll_ReturnListOfAnimesInsidePegeObject_WhenSuccessful() {

		String expectedName = AnimeCreator.createValidAnime().getName();

		Page<Anime> animePage = restRestTemplate.exchange("/animes", HttpMethod.GET,
				null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
				} ).getBody();

		assertThat(animePage).isNotNull();
		assertThat(animePage.toList()).isNotEmpty();
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	public void findById_ReturnAnAnimeInsidePegeObject_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime anime = restRestTemplate.getForObject("/animes/1", Anime.class);
		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}

	@Test
	@DisplayName("findByName returns a list of animes when successful")
	public void findByName_ReturnListOfAnimes_WhenSuccessful() {

		String expectedName = AnimeCreator.createValidAnime().getName();

		List<Anime> animeList = restRestTemplate.exchange("/animes/find?name='tensei",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
		} ).getBody();


		assertThat(animeList).isNotNull();
		assertThat(animeList).isNotEmpty();
		assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("save creates an anime when successful")
	public void save_CreatesAnime_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = restRestTemplate.exchange("/animes", HttpMethod.POST,
				createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}
	
	@Test
	@DisplayName("update save updated anime when successful")
	public void update_SaveUpdatedAnime_WhenSuccessful() {
		Anime validAnime = AnimeCreator.createValidUpdatedAnime();
		
		ResponseEntity<Void> responseEntity = restRestTemplate.exchange("/animes", HttpMethod.PUT,
				createJsonHttpEntity(validAnime), Void.class);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(responseEntity.getBody()).isNull();
	}
	
	@Test
	@DisplayName("delete removes the anime when successful")
	public void delete_RemovesAnime_WhenSuccessful() {
		
		ResponseEntity<Void> responseEntity = restRestTemplate.exchange("/animes/1", HttpMethod.DELETE,
				null, Void.class);
		
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(responseEntity.getBody()).isNull();
	}
	
	private HttpEntity<Anime> createJsonHttpEntity(Anime anime){
		return new HttpEntity<>(anime, createJsonHeader());
	}
	
	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

}
