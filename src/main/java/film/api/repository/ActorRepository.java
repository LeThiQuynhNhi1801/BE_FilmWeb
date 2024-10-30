package film.api.repository;

import film.api.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT a FROM Actor a WHERE a.ActorName LIKE %:actorName%")
    List<Actor> searchActors(@Param("actorName") String actorName);

    @Query("SELECT a FROM Actor a JOIN ActorChapter ac ON a.id = ac.Actor.id JOIN Chapter c ON ac.Chapter.Id = c.Id WHERE  c.Id = :chapterId")
    List<Actor> findActorByChapterId(@Param("chapterId") Long chapterId);
}