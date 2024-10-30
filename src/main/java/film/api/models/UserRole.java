package film.api.models;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role Role;

    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User User;
}
