package film.api.DTO.response;

import film.api.DTO.response.CategoryDTO;
import film.api.DTO.response.ChapterActorsDTO;
import film.api.models.Category;
import film.api.models.Chapter;
import film.api.models.Film;
import film.api.service.ActorService;
import film.api.service.CategoryService;
import film.api.service.ChapterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmChapterActorDTO {

    private Long Id;


    private String FilmName;


    private String FilmDescription;


    private String BannerFilmName;


    private int FilmBollen;


    private String TrailerFilm;


    private String FilmImage;
    private List<ChapterActorsDTO> chapters;
    private List<CategoryDTO> categories;


    public void loadData(Film film, ChapterService chapterService, ActorService actorService, CategoryService categoryService){
        this.Id=film.getId();
        this.FilmName=film.getFilmName();
        this.BannerFilmName=film.getBannerFilmName();
        this.FilmBollen=film.getFilmBollen();
        this.FilmImage=film.getFilmImage();
        this.FilmDescription=film.getFilmDescription();
        this.TrailerFilm =film.getTrailerFilm();
        this.chapters = new ArrayList<>();
        List<Chapter> filmChapters = chapterService.getChapterByFilmID(film.getId());
        for (Chapter chapter : filmChapters) {
            ChapterActorsDTO chapterDTO = new ChapterActorsDTO();

            chapterDTO.loadData(chapter, actorService);

            // Thêm ChapterDTO vào danh sách chapters trong FilmDTO
            this.chapters.add(chapterDTO);
        }
        this.categories =new ArrayList<>();
        List<Category> categoryList=categoryService.getCategoryByFilmID(film.getId());
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = new CategoryDTO(category);

            this.categories.add(categoryDTO);
        }
    }
}
