package br.com.gft.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.gft.domain.Anime;
import br.com.gft.util.AnimeCreator;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("Anime Repository Tests")
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnimeRepositoryTest {

	@Autowired
	private AnimeRepository animeRepository;

	@Test
	@DisplayName("Save creates anime when successful")
	public void save_PersistAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();

		Anime savedAnime = this.animeRepository.save(anime);

		assertThat(savedAnime.getId()).isNotNull();
		assertThat(savedAnime.getName()).isNotNull();
		assertThat(savedAnime.getName()).isEqualTo(anime.getName());
	}

	@Test
	@DisplayName("Save updates anime when successful")
	public void save_UpdateAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();

		Anime savedAnime = this.animeRepository.save(anime);

		savedAnime.setName("Dragon ball");

		Anime updatedAnime = this.animeRepository.save(savedAnime);

		assertThat(savedAnime.getId()).isNotNull();
		assertThat(savedAnime.getName()).isNotNull();
		assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
	}

	@Test
	@DisplayName("Delete removes anime when successful")
	public void delete_RemoveAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();

		Anime savedAnime = this.animeRepository.save(anime);

		this.animeRepository.delete(anime);

		Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

		assertThat(animeOptional.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("Find by name returns animes when successful")
	public void findByName_ReturnAnimes_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();

		Anime savedAnime = this.animeRepository.save(anime);

		String name = savedAnime.getName();

		List<Anime> animeList = this.animeRepository.findByName(name);

		assertThat(animeList).isNotEmpty();
		assertThat(animeList).contains(savedAnime);
	}

	@Test
	@DisplayName("Find by name returns empty list when no anime is found")
	public void findByName_ReturnEmptyList_WhenAnimeNotFound() {

		String name = "fake-name";

		List<Anime> animeList = this.animeRepository.findByName(name);

		assertThat(animeList).isEmpty();

	}
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when anime is empty")
	public void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
		Anime anime = new Anime();

//		assertThatThrownBy(() -> animeRepository.save(anime))
//			.isInstanceOf(ConstraintViolationException.class);
		
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> animeRepository.save(anime))
			.withMessageContaining("The name of this anime cannot be empty");
		
	}

}
