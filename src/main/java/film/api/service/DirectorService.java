package film.api.service;


import film.api.DTO.request.DirectorDTO;
import film.api.models.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    List<Director> getList();

    Director addDirector(Director director);

    Optional<Director> findById(Long id);

    Director updateDirector(Long id, DirectorDTO directorPatchDTO);

    void deleteById(Long id);

    List<Director> searchDirectors(String directorName);

    List<Director> findDirectorByChapterId(Long chapterId);
}
