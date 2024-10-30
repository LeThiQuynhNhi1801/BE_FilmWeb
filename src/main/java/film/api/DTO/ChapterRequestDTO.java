package film.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterRequestDTO {
    private  String chapterName;
    private String listActor;
    private String chapterDescription;
    private MultipartFile chapterImage;
    private MultipartFile trailerChapter;
    private MultipartFile video;
}
