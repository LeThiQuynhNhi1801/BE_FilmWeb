package film.api.DTO;

import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoryDTO {
    private Long id;


    private double WatchedTime;


    private Chapter Chapter;

    private User User;

    private Integer Rate;


    private LocalDateTime HistoryView;
    public HistoryDTO(History history){
        this.id =history.getId();
        this.Chapter=history.getChapter();
        this.HistoryView=history.getHistoryView();
        this.Rate=history.getRate();
        this.WatchedTime=history.getWatchedTime();
        this.User=history.getUser();
    }
}
