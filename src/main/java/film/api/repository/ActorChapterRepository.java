package film.api.repository;

import film.api.models.Actor;
import film.api.models.ActorChapter;
import film.api.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorChapterRepository extends JpaRepository<ActorChapter, Long> {
    @Query("SELECT ac.Chapter FROM ActorChapter ac WHERE ac.Actor.id = :actorId")
    List<Chapter> findChaptersByActorId(@Param("actorId") Long actorId);

    @Query("SELECT ac.Actor FROM ActorChapter ac WHERE ac.Chapter.id = :chapterId")
    List<Actor> findActorsByChapterId(@Param("chapterId") Long chapterId);
    @Query("SELECT ac FROM ActorChapter ac WHERE ac.Chapter.id = :chapterId and ac.Actor.id=:actorId" )
    List<ActorChapter> findActorChapterByChapterIdAndActorID(@Param("chapterId") Long chapterId,@Param("actorId") Long actorId);
    @Query("SELECT ac FROM ActorChapter ac WHERE ac.Chapter.id = :chapterId " )
    List<ActorChapter> findActorChapterByChapterId(@Param("chapterId") Long chapterId);




}