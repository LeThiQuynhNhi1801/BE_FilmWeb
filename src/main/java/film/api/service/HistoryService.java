package film.api.service;

import film.api.DTO.ChapterHotDTO;
import film.api.DTO.HistoryRequestDTO;
import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService {
    List<History> getList();

    List<ChapterHotDTO> getChaptersHotCount(LocalDateTime fromDay, LocalDateTime toDay);

    History getHistory(Long idChapter, Long userID);

    Long getUserID(String username);

    History saveHistory(History history);

    History updateHistory(User user, Chapter chapter, HistoryRequestDTO historyPatch);

    List<History> getListhistory(Long id);

    List<Chapter> findChaptersByUserId(Long filmID);

    List<Chapter> getChaptersHot(LocalDateTime fromDay, LocalDateTime toDay);
}
