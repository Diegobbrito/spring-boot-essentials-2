package br.com.gft.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import br.com.gft.domain.Anime;
import br.com.gft.exception.ResourceNotFoundException;
import br.com.gft.repository.AnimeRepository;

@Service
public class Utils {
	public String formatLocalDateTimeToDatavaseStyle(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
	
	public Anime findAnimeOtThrowNotFound(int id, AnimeRepository animeRepository) {
		return animeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Anime Not Found"));
			
	}
}
