package film.api.DTO;

import film.api.models.Chapter;
import film.api.models.History;
import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRequestDTO {
    private Long id;


    private Double WatchedTime;


    private Integer Rate;


    private LocalDateTime HistoryView;

}
