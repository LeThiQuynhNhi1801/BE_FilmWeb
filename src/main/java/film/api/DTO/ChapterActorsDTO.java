package film.api.DTO;

import film.api.models.Actor;
import film.api.models.Chapter;
import film.api.service.ActorService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterActorsDTO {


    private Long Id;


    private String ChapterName;


    private int ChapterNumber;


    private String Video;




    private String ChapterDescription;

    private String TrailerChapter;


    private String ChapterImage;

    private LocalDateTime ChapterCreateDay;

    private LocalDateTime ChapterPremieredDay;

    private String ChapterStatus;

    private List<Actor> actors;
    public void loadData(Chapter chapter, ActorService actorService)
    {
        this.Id=chapter.getId();
        this.ChapterCreateDay=chapter.getChapterCreateDay();
        this.ChapterDescription=chapter.getChapterDescription();
        this.ChapterImage=chapter.getChapterImage();
        this.ChapterNumber=chapter.getChapterNumber();
        this.ChapterName=chapter.getChapterName();
        this.ChapterStatus=chapter.getChapterStatus();
        this.Video=chapter.getVideo();
        this.TrailerChapter=chapter.getTrailerChapter();
        this.ChapterPremieredDay=chapter.getChapterPremieredDay();
        this.actors = actorService.findActorByChapterId(chapter.getId());

    }


}
