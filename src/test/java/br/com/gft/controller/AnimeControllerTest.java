package br.com.gft.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.gft.domain.Anime;
import br.com.gft.service.AnimeService;
import br.com.gft.util.AnimeCreator;

@ExtendWith(SpringExtension.class)
public class AnimeControllerTest {

	@InjectMocks
	private AnimeController animeController;

	@Mock
	private AnimeService animeServiceMock;

	@BeforeEach
	public void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

		BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyInt()))
				.thenReturn(AnimeCreator.createValidAnime());

		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeServiceMock.save(AnimeCreator.createAnimeToBeSaved()))
		.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeServiceMock)
		.delete(ArgumentMatchers.anyInt());
		
		BDDMockito.when(animeServiceMock.save(AnimeCreator.createValidAnime()))
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
	@DisplayName("findByName returns a list of animes when successful")
	public void findByName_ReturnListOfAnimes_WhenSuccessful() {

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
