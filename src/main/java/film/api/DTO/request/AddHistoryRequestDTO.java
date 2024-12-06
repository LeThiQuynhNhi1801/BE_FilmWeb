package film.api.DTO.request;

import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddHistoryRequestDTO {

    private Double WatchedTime;


    private Integer Rate;


    private LocalDateTime HistoryView;

    private String device;

    private String weather;

    private Timestamp time;

}
