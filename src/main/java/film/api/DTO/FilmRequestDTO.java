package film.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class FilmRequestDTO {
        private String filmName;
        private String   description;
        private Integer   filmBollen;

        private String listCategory;
        private String   listActor;
        private MultipartFile image;
        private MultipartFile  bannerFilmName;
        private MultipartFile trailerFilm;
        private MultipartFile  video;


        private String chapterName;
        private String chapterDescription;
}
