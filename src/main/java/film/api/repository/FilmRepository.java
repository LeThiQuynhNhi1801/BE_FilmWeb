package film.api.repository;

import film.api.models.Film;
import film.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f where f.FilmName LIKE %:filmName%")
    List<Film> findUsersByFilmNameContain(@Param("filmName") String filmName);
    @Query("SELECT f from Film f join Chapter c on c.Film.Id = f.Id where c.Id = :id")
    Film filmByIdChapter(@Param("id") Long id);

    @Query("SELECT f FROM Film f JOIN CategoryFilm cf ON f.Id = cf.Film.Id WHERE cf.Category.id =:categoryID")
    List<Film> categoryAllFim(@Param("categoryID") Long categoryID);
    @Query("SELECT f FROM Film f join Chapter c on f.Id = c.Film.Id join ActorChapter ac on ac.Chapter.Id = c.Id join Actor a on ac.Actor.id = a.id where f.FilmName LIKE  %:key% or a.ActorName LIKE %:key% or f.FilmDescription LIKE %:key%")
    List<Film> searchFilm(@Param("key") String key);
}