package film.api.repository;

import film.api.models.ActorChapter;
import film.api.models.CategoryFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryFilmRepository extends JpaRepository<CategoryFilm, Long> {
    @Query("SELECT cf FROM CategoryFilm cf WHERE cf.Film.Id = :filmID " )
    List<CategoryFilm> findCategoryFilmByFilmID(@Param("filmID") Long filmID);
}
