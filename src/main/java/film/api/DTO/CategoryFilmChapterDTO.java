package film.api.DTO;


import film.api.models.Category;
import film.api.models.Chapter;
import film.api.models.Film;

import film.api.service.CategoryService;
import film.api.service.ChapterService;
import film.api.service.FilmService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoryFilmChapterDTO {
    private Long id;
    private String CategoryName;
    private List<FilmChaptersDTO> films;


    public void loadData(Category category, ChapterService chapterService, FilmService filmService, CategoryService categoryService){
        this.id=category.getId();
        this.CategoryName = category.getCategoryName();
        this.films = new ArrayList<>();
        List<Film> filmByCategory = filmService.filmByCategoryId(category.getId());
        for (Film film : filmByCategory) {
            List<ChapterDTO> list = new ArrayList<>();
            List<Chapter> chapterList = chapterService.getChapterByFilmID(film.getId());
            for (Chapter chapter : chapterList){

                list.add(new ChapterDTO(chapter));
            }
            FilmChaptersDTO filmChaptersDTO = new FilmChaptersDTO(film);
            filmChaptersDTO.setChapters(list);
            this.films.add(filmChaptersDTO);
        }
    }
}