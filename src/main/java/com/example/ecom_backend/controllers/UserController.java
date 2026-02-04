package com.example.ecom_backend.controllers;

import com.example.ecom_backend.config.AppConfigurationProperties;
import com.example.ecom_backend.dtos.UserLoginDTO;
import com.example.ecom_backend.dtos.UserSignUpDTO;
import com.example.ecom_backend.entities.AppUser;
import com.example.ecom_backend.exceptions.WrongUserCredentials;
import com.example.ecom_backend.repositories.UserRepo;
import com.example.ecom_backend.services.UserService;
import com.example.ecom_backend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    AppConfigurationProperties properties;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/testEverybody")
    public String testEverybody(){
        return "everybody can see this";
    }

    @GetMapping("/testOnlyUser")
    public String testOnlyStudent(){
        return "users can see this";
    }

    @GetMapping("/testOnlyAdmin")
    public String testOnlyAdmin(){
        return "only admins should be abe to see this";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody UserSignUpDTO dto){

        AppUser appUser = new AppUser();

        userService.createUser(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                dto.getAddress()
        );

        return "user created";
    }

    @PostMapping("/createAdmin")
    public String createAdmin(@RequestBody UserSignUpDTO dto){

        AppUser appUser = new AppUser();

        userService.createAdmin(
                dto.getUsername(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail(),
                dto.getAddress()
        );

        return "admin created";
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO dto, HttpServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = userService.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            ResponseCookie cookie = ResponseCookie.from(properties.getCookie().getName(), jwt)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(properties.getCookie().getExpiresIn()) // TODO : this is hard coded here now. make it get it from the application properties next time
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok("login successful");
        }
        catch (BadCredentialsException | UsernameNotFoundException e){
            throw new WrongUserCredentials("Incorrect username or password");
        }
        catch(Exception e){
            throw new WrongUserCredentials("emo ra babu edho ayyindhi user controller layer lo");
        }
    }


}
