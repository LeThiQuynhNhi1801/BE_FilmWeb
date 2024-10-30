package film.api.service;

import film.api.DTO.UserByAdminDTO;
import film.api.configuration.security.UserChangePassword;
import film.api.models.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    User findByUsername(String username);

    List<User> findUsersByNameContain(String name);

    User updateUser(User user);

    User save(User user);

    User ChangePassword(String username, UserChangePassword userChangePassword);

    void deleteById(Long id);

    List<User> findAll();

    User updateUser(Long id, UserByAdminDTO userPatchDTO);
}
