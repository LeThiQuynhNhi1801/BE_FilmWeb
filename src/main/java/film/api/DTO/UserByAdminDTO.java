package film.api.DTO;
import film.api.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Data
@AllArgsConstructor
public class UserByAdminDTO {
    private Long Id;


    private String username;


    private String password;


    private String fullname;
    public  UserByAdminDTO(User user){
        this.fullname=user.getFullname();
        this.Id=user.getId();

        this.username=user.getUsername();


    }
}
