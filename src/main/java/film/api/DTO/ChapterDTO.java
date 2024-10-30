package film.api.DTO;

import film.api.models.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {

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

        public  ChapterDTO(Chapter chapter)
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
        }

}
