package film.api.controller;

import film.api.DTO.UserByAdminDTO;
import film.api.models.User;
import film.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ApiV1")

public class UsersController {
    @Autowired
    private UserService userService;
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/AllUser")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.findAll();

        List<UserByAdminDTO> userListDTO= new ArrayList<>();
        for(User user:userList){
            userListDTO.add(new UserByAdminDTO(user));
        }
        return ResponseEntity.ok(userListDTO);
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("UserByIDForAdmin/{UserID}")
    public ResponseEntity<?> getUserByID(@PathVariable("UserID") Long id){
        User user=userService.findById(id);
        UserByAdminDTO userDTO= new UserByAdminDTO(user);
        return ResponseEntity.ok(userDTO);
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("UserByName/{Username}")
    public ResponseEntity<?> UserByName(@PathVariable("Username") String username){
        List<User> users=userService.findUsersByNameContain(username);
        List<UserByAdminDTO> userListDTO=new ArrayList<>();
        for (User user:users){
            userListDTO.add(new UserByAdminDTO(user));
        }

        return ResponseEntity.ok(userListDTO);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("UserByIDForAdmin/{UserID}")
    public ResponseEntity<?> deleteUserByID(@PathVariable("UserID") Long id){
        userService.deleteById(id);
        return new ResponseEntity<>("Xoa thanh cong", HttpStatus.NO_CONTENT);
    }
    @Secured({"ROLE_ADMIN"})
    @PatchMapping("UserByIDForAdmin/{UserID}")
    public User updateUser(@PathVariable("UserID") Long id,
                           @RequestBody UserByAdminDTO userPatchDTO) {
        return userService.updateUser(id, userPatchDTO);
    }

}
