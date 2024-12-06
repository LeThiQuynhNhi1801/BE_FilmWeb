package film.api.configuration.security;
import film.api.DTO.response.UserSignupDTO;
import film.api.models.*;
import film.api.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public User save(UserSignupDTO user, PasswordEncoder bcryptEncoder) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFullname(user.getFullName());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userService.save(newUser);
    }
    public User changePassword(String username,UserChangePassword user, PasswordEncoder bcryptEncoder) {

        user.setPasswordNew(bcryptEncoder.encode(user.getPasswordNew()));
        return userService.ChangePassword(username,user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
