package film.api.configuration.security.controller;

import film.api.configuration.security.*;
import film.api.models.User;
import film.api.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("/ApiV1/ChangePassword")

    public ResponseEntity<?> ChangePassword(HttpServletRequest request, @RequestBody UserChangePassword userChangePassword) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userChangePassword.getPassword()));
        User userPatchPassword=jwtUserDetailsService.changePassword(username,userChangePassword, passwordEncoder);
        return ResponseEntity.ok(userPatchPassword);
    }


}
