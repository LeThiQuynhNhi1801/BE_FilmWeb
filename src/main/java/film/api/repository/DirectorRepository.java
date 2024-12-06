package film.api.repository;

import film.api.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    @Query("SELECT a FROM Director a WHERE a.DirectorName LIKE %:directorName%")
    List<Director> searchDirectors(@Param("directorName") String directorName);

    @Query("SELECT a FROM Director a " +
            "JOIN DirectorChapter ac ON a.id = ac.Director.id " +
            "JOIN Chapter c ON ac.Chapter.Id = c.Id " +
            "WHERE  c.Id = :chapterId")
    List<Director> findDirectorByChapterId(@Param("chapterId") Long chapterId);
}
