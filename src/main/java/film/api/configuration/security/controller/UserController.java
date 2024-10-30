package film.api.configuration.security.controller;

import film.api.DTO.UserDTO;
import film.api.DTO.UserSignupDTO;
import film.api.configuration.security.*;
import film.api.models.User;
import film.api.service.ChapterService;
import film.api.service.HistoryService;
import film.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;
    private final JWTUtil jwtUtil;
    @Qualifier("jwtUserDetailsService")
    private final UserDetailsService userDetailsService;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final HistoryService historyService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserSignupDTO user) throws Exception {
        return new ResponseEntity<>(jwtUserDetailsService.save(user, passwordEncoder),HttpStatus.CREATED);
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/ApiV1/UserByLogin")
    public ResponseEntity<?> getUserByIdLogin(HttpServletRequest request){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        User user = userService.findById(userID);
        UserDTO userDTO = new UserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("/ApiV1/UserByLogin")
    public ResponseEntity<?> changeFullName(HttpServletRequest request,@RequestBody UserDTO userDTO){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        User user = userService.findById(userID);
        user.setFullname(userDTO.getFullName());
        User userUpdate = userService.updateUser(user);
        UserDTO userUpdateDTO = new UserDTO(userUpdate);
        return new ResponseEntity<>(userUpdateDTO, HttpStatus.OK);
    }


}