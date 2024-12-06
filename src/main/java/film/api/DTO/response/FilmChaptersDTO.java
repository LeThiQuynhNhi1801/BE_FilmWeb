package film.api.DTO.response;

import film.api.DTO.response.ChapterDTO;
import film.api.models.Film;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilmChaptersDTO {

    private Long Id;


    private String FilmName;


    private String FilmDescription;


    private String BannerFilmName;


    private int FilmBollen;


    private String TrailerFilm;


    private String FilmImage;
    private List<ChapterDTO> chapters;
    public FilmChaptersDTO(Film film){
        this.Id=film.getId();
        this.FilmName=film.getFilmName();
        this.BannerFilmName=film.getBannerFilmName();
        this.FilmBollen=film.getFilmBollen();
        this.FilmImage=film.getFilmImage();
        this.FilmDescription=film.getFilmDescription();
        this.TrailerFilm =film.getTrailerFilm();

    }
}
