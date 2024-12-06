package film.api.service;

import film.api.DTO.request.HistoryRequestDTO;
import film.api.DTO.response.ChapterHotDTO;
import film.api.DTO.response.HistoryDTO;
import film.api.DTO.request.AddHistoryRequestDTO;
import film.api.models.Chapter;
import film.api.models.History;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService {
    List<History> getList();

    List<ChapterHotDTO> getChaptersHotCount(LocalDateTime fromDay, LocalDateTime toDay);

    HistoryDTO getHistory(Long idChapter, Long userID);

    Long getUserID(String username);

    HistoryDTO saveHistory(Long chapterId, AddHistoryRequestDTO historyRequestDTO, String username);

    HistoryDTO updateHistory(HistoryRequestDTO historyPatch, Long chapterId,String username);

    List<History> getListhistory(Long id);

    List<Chapter> findChaptersByUserId(Long filmID);

    List<Chapter> getChaptersHot(LocalDateTime fromDay, LocalDateTime toDay);


}
