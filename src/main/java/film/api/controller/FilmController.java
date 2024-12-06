package film.api.controller;

import film.api.DTO.request.FilmRequestDTO;
import film.api.DTO.response.*;
import film.api.models.Category;
import film.api.models.Chapter;
import film.api.models.Film;
import film.api.service.ActorService;
import film.api.service.CategoryService;
import film.api.service.ChapterService;
import film.api.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/ApiV1")
@Slf4j
@CrossOrigin("*")
public class FilmController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private CategoryService categoryService;
    @Secured({"ROLE_ADMIN","USER_ADMIN"})
    @GetMapping("/AllFilm")
    public ResponseEntity<?> getFilmList(){
        List<Film> films=filmService.findAll();
        List<FilmDTO> filmListDTO =new ArrayList<>();
        for(Film film:films){
            filmListDTO.add(new FilmDTO(film));
        }
        return new ResponseEntity<>(films, HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/FilmByID/{FilmID}")
    public ResponseEntity<?> GetFilmByID(@PathVariable("FilmID") Long FilmID){
        Film film = filmService.findById(FilmID);
        FilmChapterActorDTO filmChapterActorDTO= new FilmChapterActorDTO();
        filmChapterActorDTO.loadData(film,chapterService,actorService,categoryService);
        return new ResponseEntity<>(filmChapterActorDTO, HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/AllFilm")
    public ResponseEntity<?> postFilm(@ModelAttribute FilmRequestDTO filmPost){
        Film film =filmService.saveFilm(filmPost);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/FilmByName/{filmName}")
    public ResponseEntity<?> getFilmByID(@PathVariable("filmName") String filmName){
        List<Film> films=filmService.findUsersByFilmNameContain(filmName);
        List<FilmDTO> filmListDTO=new ArrayList<>();
        for (Film film:films){
            filmListDTO.add(new FilmDTO(film));
        }

        return ResponseEntity.ok(filmListDTO);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/FilmByID/{FilmID}")
    public ResponseEntity<?> DeleteFilm(@PathVariable("FilmID") Long FilmID){
        filmService.deleteFilmByID(FilmID);
        return new ResponseEntity<>("Xoa thanh cong", HttpStatus.NO_CONTENT);
    }
    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/FilmByID/{FilmID}")
    public ResponseEntity<?> PatchFilm(@PathVariable("FilmID") Long FilmID,@ModelAttribute FilmRequestDTO filmRequestDTO){
        Film film =filmService.updateFilm(FilmID,filmRequestDTO);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/FilmChapterIDChapterID/{chapterId}")
    public ResponseEntity<?> FilmChapterIDChapterID(@PathVariable("chapterId") Long chapterId){
        Film film = filmService.filmByIdChapter(chapterId);
        FilmChaptersDTO filmChaptersDTO = new FilmChaptersDTO(film);
        List<Chapter> chapterList = chapterService.chapterByChapterId(chapterId);
        List<ChapterDTO> chapterDTOList = new ArrayList<>();
        for (Chapter chapter : chapterList) {

            chapterDTOList.add(new ChapterDTO(chapter));
        }
        filmChaptersDTO.setChapters(chapterDTOList);
        return new ResponseEntity<>(filmChaptersDTO,HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("CategoryAllFim")
    public ResponseEntity<?> GetCategoryFilmList(){
        List<CategoryFilmChapterDTO> list = new ArrayList<>();
        List<Category> categories = categoryService.findAll();
        for (Category category : categories){
            CategoryFilmChapterDTO cfc = new CategoryFilmChapterDTO();
            cfc.loadData(category,chapterService,filmService,categoryService);
            list.add(cfc);

        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN,ROLE_USER"})
    @GetMapping("/FindFilm/{key}")
    public ResponseEntity<?> searchFilm(@PathVariable("key") String key) {
        List<Film> films = filmService.searchFilm(key);

        List<FilmChaptersDTO> chaptersDTOS =new ArrayList<>();
        for (Film film : films) {
            FilmChaptersDTO filmChaptersDTO = new FilmChaptersDTO(film);
            List<Chapter> chapterList = chapterService.getChapterByFilmID(film.getId());
            List<ChapterDTO> chapterDTOList = new ArrayList<>();
            for (Chapter chapter : chapterList) {

                chapterDTOList.add(new ChapterDTO(chapter));
            }
            filmChaptersDTO.setChapters(chapterDTOList);
            chaptersDTOS.add(filmChaptersDTO);
        }

        return new ResponseEntity(chaptersDTOS, HttpStatus.OK);
    }
}
