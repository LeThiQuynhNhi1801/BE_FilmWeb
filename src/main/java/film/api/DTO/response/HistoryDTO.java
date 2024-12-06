package film.api.DTO.response;

import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {
    private Long id;


    private double WatchedTime;


    private Chapter Chapter;

    private User User;

    private Integer Rate;

    private String device;

    private String weather;

    private Timestamp time;


    private LocalDateTime HistoryView;

}
