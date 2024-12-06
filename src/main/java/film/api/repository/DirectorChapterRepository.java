package film.api.repository;

import film.api.models.Director;
import film.api.models.DirectorChapter;
import film.api.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorChapterRepository extends JpaRepository<DirectorChapter, Long> {
    @Query("SELECT ac.Chapter FROM DirectorChapter ac WHERE ac.Director.id = :directorId")
    List<Chapter> findChaptersByDirectorId(@Param("directorId") Long directorId);

    @Query("SELECT ac.Director FROM DirectorChapter ac WHERE ac.Chapter.Id = :chapterId")
    List<Director> findDirectorsByChapterId(@Param("chapterId") Long chapterId);
    @Query("SELECT ac FROM DirectorChapter ac WHERE ac.Chapter.Id = :chapterId and ac.Director.id=:directorId" )
    List<DirectorChapter> findDirectorChapterByChapterIdAndDirectorID(@Param("chapterId") Long chapterId,@Param("directorId") Long directorId);
    @Query("SELECT ac FROM DirectorChapter ac WHERE ac.Chapter.Id = :chapterId " )
    List<DirectorChapter> findDirectorChapterByChapterId(@Param("chapterId") Long chapterId);




}