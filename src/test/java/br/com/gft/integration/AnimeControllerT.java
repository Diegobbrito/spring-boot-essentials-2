package br.com.gft.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.gft.controller.AnimeController;
import br.com.gft.domain.Anime;
import br.com.gft.repository.AnimeRepository;
import br.com.gft.service.AnimeService;
import br.com.gft.util.AnimeCreator;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnimeControllerT {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
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

		Page<Anime> animePage = animeController.listAll(null).getBody();

		assertThat(animePage).isNotNull();
		assertThat(animePage.toList()).isNotEmpty();
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	public void findById_ReturnAnAnimeInsidePegeObject_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime anime = animeController.findById(1).getBody();

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}

	@Test
	@DisplayName("findByName returns a pageable list of animes when successful")
	public void findByName_ReturnListOfAnimesInsidePegeObject_WhenSuccessful() {

		String expectedName = AnimeCreator.createValidAnime().getName();

		List<Anime> animeList = animeController.findByName("DBZ").getBody();

		assertThat(animeList).isNotNull();
		assertThat(animeList).isNotEmpty();
		assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("save creates an anime when successful")
	public void save_CreatesAnime_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = animeController.save(animeToBeSaved).getBody();

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}
	
	@Test
	@DisplayName("update save updated anime when successful")
	public void update_SaveUpdatedAnime_WhenSuccessful() {
		
		ResponseEntity<Void> responseEntity = animeController.update(AnimeCreator.createValidAnime());

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(responseEntity.getBody()).isNull();
	}
	
	@Test
	@DisplayName("delete removes the anime when successful")
	public void delete_RemovesAnime_WhenSuccessful() {
		
		ResponseEntity<Void> responseEntity = animeController.delete(1);
		
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(responseEntity.getBody()).isNull();
	}

}
