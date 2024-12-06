package film.api.models;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double WatchedTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter Chapter;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User User;

    private int Rate;

    @NotNull
    private LocalDateTime HistoryView;

    private String weather;

    private String device;

    private Timestamp time;
}
