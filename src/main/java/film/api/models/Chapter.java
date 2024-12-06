package film.api.models;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String idChapter;

    @NotNull
    private String ChapterName;

    @NotNull
    private int ChapterNumber;

    @NotNull
    private String Video;

    @NotNull
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film Film;

    @NotNull
    private String ChapterDescription;

    @NotNull
    private String TrailerChapter;

    @NotNull
    private String ChapterImage;

   @Column(nullable = true)
    private LocalDateTime ChapterCreateDay;

    @Column(nullable = true)
    private LocalDateTime ChapterPremieredDay;

    @NotNull
    private String ChapterStatus;

//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "actor_id")
//    private Actor actor;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "director_id")
//    private Director director;
}
