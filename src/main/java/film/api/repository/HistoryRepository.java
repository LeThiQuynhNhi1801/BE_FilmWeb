package film.api.repository;

import film.api.models.Chapter;
import film.api.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(nativeQuery = true, value = "SELECT history.chapter_id, COUNT(history.id) AS count, AVG(history.rate) AS avg " +
            "FROM history JOIN chapter " +
            "WHERE (history.history_view >= :fromDay AND history.history_view < :toDay) " +
            "GROUP BY history.Chapter_id ORDER BY count DESC")
    List<Object[]> getChaptersHotCount(@Param("fromDay") LocalDateTime fromDay, @Param("toDay") LocalDateTime toDay);
    @Query("SELECT h from History h where h.User.Id = :userID and h.Chapter.Id = :idChapter")
    History historyByUserIDAndChapterID(@Param("userID") Long userID, @Param("idChapter") Long idChapter);

    @Query("SELECT user.Id from User user WHERE user.username = :key")
    Long useridByUserName(@Param("key") String key);
    @Query("SELECT h FROM History h WHERE h.User.Id = :id")
    List<History> historyByIdUser(@Param("id") Long id);
    @Query("SELECT h.Chapter FROM History h WHERE h.User.id = :userID")
    List<Chapter> findChaptersByUserId(@Param("userID") Long userID);

    @Query("select h.Rate from History h where h.User.Id=:idUser and h.Chapter.Id=:idChapter")
    int ratingbychapteranduser(@Param("idUser") Long idUser, @Param("idChapter") Long idChapter);


}