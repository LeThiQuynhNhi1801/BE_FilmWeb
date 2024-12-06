package film.api.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class UserSignupDTO {
    private String username;
    private String password;
    private String fullName;
}
