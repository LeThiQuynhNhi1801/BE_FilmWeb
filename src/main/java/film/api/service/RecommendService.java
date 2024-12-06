package film.api.service;

import film.api.DTO.request.ContextRequestDTO;
import film.api.models.Chapter;
import film.api.models.Film;
import film.api.models.History;

import java.time.LocalDateTime;
import java.util.List;

public interface RecommendService {
    List<Chapter> recommend(String username, ContextRequestDTO contextRequestDTO, int N);
}
