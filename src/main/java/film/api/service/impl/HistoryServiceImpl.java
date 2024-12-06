package film.api.service.impl;

import film.api.DTO.request.HistoryRequestDTO;
import film.api.DTO.response.ChapterHotDTO;
import film.api.DTO.response.HistoryDTO;
import film.api.DTO.request.AddHistoryRequestDTO;
import film.api.configuration.security.JWTUtil;
import film.api.exception.NotFoundException;
import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;
import film.api.repository.ChapterRepository;
import film.api.repository.HistoryRepository;
import film.api.repository.UserRepository;
import film.api.service.HistoryService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JWTUtil jwtUtil;
    @Override
    public List<History> getList() {

        return historyRepository.findAll();
    }

    @Override
    public List<ChapterHotDTO> getChaptersHotCount(LocalDateTime fromDay, LocalDateTime toDay) {
        List<Object[]> results = historyRepository.getChaptersHotCount(fromDay, toDay);
        List<ChapterHotDTO> chapterHotDTOs = new ArrayList<>();
        for (Object[] result : results) {
            ChapterHotDTO chapterHotDTO = new ChapterHotDTO();
            BigInteger bigInt = (BigInteger) result[0];
            Long chapterID = bigInt.longValue();
            Optional<Chapter> chapter = chapterRepository.findById(chapterID);
            chapterHotDTO.setId((BigInteger) result[0]);
            chapterHotDTO.setCountView((BigInteger) result[1]);
            chapterHotDTO.setRateAvg((BigDecimal) result[2]);
            chapterHotDTO.setChapterImage(chapter.get().getChapterImage());
            chapterHotDTO.setChapterName(chapter.get().getChapterName());
            chapterHotDTOs.add(chapterHotDTO);
        }
        return chapterHotDTOs;
    }

    @Override
    public HistoryDTO getHistory(Long idChapter, Long userID) {
        try {
            History history = historyRepository.historyByUserIDAndChapterID(userID, idChapter);
            HistoryDTO historyDTO = new HistoryDTO(history.getId(),history.getWatchedTime(),history.getChapter(),history.getUser(),history.getRate(),history.getDevice(),history.getWeather(),history.getTime(),history.getHistoryView());
            return historyDTO;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Long getUserID(String username) {
        Long userid = historyRepository.useridByUserName(username);
        return userid;
    }

    @Override
    public HistoryDTO saveHistory(Long chapterId, AddHistoryRequestDTO historyRequestDTO,String username) {
        User user =userRepository.findByUsername(username);
        Chapter chapter = chapterRepository.findById(chapterId).get();
        History history = new History(null,historyRequestDTO.getWatchedTime(),chapter,user,historyRequestDTO.getRate(),historyRequestDTO.getHistoryView(),historyRequestDTO.getWeather(),historyRequestDTO.getDevice(),historyRequestDTO.getTime());
        historyRepository.save(history);
        HistoryDTO historyDTO = new HistoryDTO(history.getId(),history.getWatchedTime(),history.getChapter(),history.getUser(),history.getRate(),history.getDevice(),history.getWeather(),history.getTime(),history.getHistoryView());
        return historyDTO;
    }

    @Override
    public HistoryDTO updateHistory(HistoryRequestDTO historyPatch,Long chapterId,String username) {
        User user =userRepository.findByUsername(username);
        if(user ==null){
            throw new NotFoundException("User này không tồn tại");
        }
        Chapter chapter=chapterRepository.findById(chapterId).get();
        if(chapter ==null){
            throw new NotFoundException("Chapter này không tồn tại");
        }
        History history = historyRepository.historyByUserIDAndChapterID(user.getId(), chapter.getId());
        if (historyPatch.getRate() != null) {
            history.setRate(historyPatch.getRate());
        }
        if (historyPatch.getHistoryView() != null) {
            history.setHistoryView(historyPatch.getHistoryView());
        }
        if (historyPatch.getWatchedTime() != null) {
            history.setWatchedTime(historyPatch.getWatchedTime());
        }
        historyRepository.save(history);
        HistoryDTO historyDTO = new HistoryDTO(history.getId(),history.getWatchedTime(),history.getChapter(),history.getUser(),history.getRate(),history.getDevice(),history.getWeather(),history.getTime(),history.getHistoryView());
        return historyDTO;
    }

    @Override
    public List<History> getListhistory(Long id) {
        return historyRepository.historyByIdUser(id);
    }

    @Override
    public List<Chapter> findChaptersByUserId(Long filmID) {
        return historyRepository.findChaptersByUserId(filmID);
    }

    @Override
    public List<Chapter> getChaptersHot(LocalDateTime fromDay, LocalDateTime toDay) {
        List<Object[]> results = historyRepository.getChaptersHotCount(fromDay, toDay);
        List<Chapter> chapterHots = new ArrayList<>();
        for (Object[] result : results) {
            ChapterHotDTO chapterHotDTO = new ChapterHotDTO();
            BigInteger bigInt = (BigInteger) result[0];
            Long chapterID = bigInt.longValue();
            Chapter chapter = chapterRepository.findById(chapterID)
                    .orElseThrow(() -> new NotFoundException("Chapter not found"));
            if (chapter != null) chapterHots.add(chapter);

        }
        return chapterHots;
    }
}
