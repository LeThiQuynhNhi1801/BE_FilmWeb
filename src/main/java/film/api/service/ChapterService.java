package film.api.service;

import film.api.DTO.ChapterRequestDTO;
import film.api.models.Chapter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChapterService {
    String getUniqueFileName(String fileName, String uploadDir);

    String saveFile(MultipartFile file, String typeFile);

    List<Chapter> getList();

    List <Chapter> getChapterByFilmID(Long filmID);

    List<Chapter> chapterByChapterId(Long chapterId);

    Chapter getChapter(Long id);

    Chapter findByID(Long chapterID);

    Chapter addChapter(Long filmID, ChapterRequestDTO chapterPost);

    Chapter updateChapter(Long chapterID, ChapterRequestDTO chapterPatch);

    List<Chapter> findAllByNotInId(List<Long> chapterIDList);

    List<Chapter> newestChapters();
}
