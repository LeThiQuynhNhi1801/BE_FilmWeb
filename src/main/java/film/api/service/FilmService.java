package film.api.service;

import film.api.DTO.FilmRequestDTO;
import film.api.models.Film;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FilmService {
    String getUniqueFileName(String fileName, String uploadDir);

    String saveFile(MultipartFile file, String typeFile);

    Film findById(Long id);

    Film saveFilm(FilmRequestDTO filmPost);

    Film updateFilm(Long filmID, FilmRequestDTO filmPatch);

    List<Film> findUsersByFilmNameContain(String filmName);

    void deleteFilmByID(Long filmID);

    Film save(Film film);

    void deleteById(Long id);

    List<Film> findAll();

    Film filmByIdChapter(Long chapterId);

    List<Film> filmByCategoryId(Long categoryId);

    List<Film> searchFilm(String key);
}
