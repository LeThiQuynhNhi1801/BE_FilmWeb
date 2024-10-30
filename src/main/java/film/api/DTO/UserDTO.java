package film.api.DTO;

import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long Id;


    private String username;


    private String fullName;

    public UserDTO(User user) {
        Id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullname();
    }
}