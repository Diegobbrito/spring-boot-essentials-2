package br.com.gft.util;

import br.com.gft.domain.Anime;

public class AnimeCreator {
	
	public static Anime createAnimeToBeSaved() {
		return Anime.builder()
				.name("Tensei Shitara")
				.id(1)
				.build();
	}
	
	public static Anime createValidAnime() {
		return Anime.builder()
				.name("Tensei Shitara")
				.id(1)
				.build();
	}
	
	public static Anime createValidUpdatedAnime() {
		return Anime.builder()
				.name("Tensei Shitara 2")
				.id(1)
				.build();
	}

}
