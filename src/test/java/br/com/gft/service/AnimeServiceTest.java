package br.com.gft.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.gft.domain.Anime;
import br.com.gft.exception.ResourceNotFoundException;
import br.com.gft.repository.AnimeRepository;
import br.com.gft.util.AnimeCreator;
import br.com.gft.util.Utils;

@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {
	@InjectMocks
	private AnimeService animeService;
	
	@Mock
	private AnimeRepository animeRepositoryMock;
	
	@Mock
	private Utils utilsMock;

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
		
		BDDMockito.when(utilsMock.findAnimeOtThrowNotFound(ArgumentMatchers.anyInt(), 
				ArgumentMatchers.any(AnimeRepository.class)))
				.thenReturn(AnimeCreator.createValidAnime());
		}

	@Test
	@DisplayName("listAll returns a pageable list of animes when successful")
	public void listAll_ReturnListOfAnimesInsidePegeObject_WhenSuccessful() {

		String expectedName = AnimeCreator.createValidAnime().getName();

		Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

		assertThat(animePage).isNotNull();
		assertThat(animePage.toList()).isNotEmpty();
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	public void findById_ReturnAnAnimeInsidePegeObject_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime anime = animeService.findById(1);

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}

	@Test
	@DisplayName("findByName returns a pageable list of animes when successful")
	public void findByName_ReturnListOfAnimesInsidePegeObject_WhenSuccessful() {

		String expectedName = AnimeCreator.createValidAnime().getName();

		List<Anime> animeList = animeService.findByName("DBZ");

		assertThat(animeList).isNotNull();
		assertThat(animeList).isNotEmpty();
		assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
	}

	@Test
	@DisplayName("save creates an anime when successful")
	public void save_CreatesAnime_WhenSuccessful() {

		Integer expextedId = AnimeCreator.createValidAnime().getId();

		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = animeService.save(animeToBeSaved);

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getId()).isEqualTo(expextedId);
	}
	
	@Test
	@DisplayName("save updating anime when successful")
	public void save_SaveUpdatedAnime_WhenSuccessful() {
		
		Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();
		
		String expectedName = validUpdatedAnime.getName();
		
		Anime anime = animeService.save(AnimeCreator.createValidAnime());

		assertThat(anime).isNotNull();
		assertThat(anime.getId()).isNotNull();
		assertThat(anime.getName()).isEqualTo(expectedName);
	}
	
	@Test
	@DisplayName("delete removes the anime when successful")
	public void delete_RemovesAnime_WhenSuccessful() {
		assertThatCode(() -> animeService.delete(1))
			.doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("delete throws ResourceNotFoundException when the anime does not exist")
	public void delete_ThrowsResourceNotFoundException_WhenAnimeDoesNotExists() {
		
		BDDMockito.when(
				utilsMock.findAnimeOtThrowNotFound(ArgumentMatchers.anyInt(), 
				ArgumentMatchers.any(AnimeRepository.class)))
				.thenThrow(new ResourceNotFoundException("Anime not found"));
		
		assertThatExceptionOfType(ResourceNotFoundException.class)
		.isThrownBy(() -> animeService.delete(1));
	}

}
