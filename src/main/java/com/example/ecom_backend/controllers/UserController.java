package com.example.ecom_backend.controllers;

import com.example.ecom_backend.dtos.UserLoginDTO;
import com.example.ecom_backend.dtos.UserSignUpDTO;
import com.example.ecom_backend.entities.AppUser;
import com.example.ecom_backend.repositories.UserRepo;
import com.example.ecom_backend.services.UserService;
import com.example.ecom_backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
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

//        appUser.setName(dto.getName());
//        appUser.setPassword(dto.getPassword());
//        appUser.setAddress(dto.getAddress());
//        appUser.setEmail(dto.getEmail());
//        appUser.setUsername(dto.getUsername());

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
    public ResponseEntity<String> login(@RequestBody UserLoginDTO dto){
        String username = dto.getUsername();
        String password = dto.getPassword();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = userService.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e){
            log.error("exception occurred while createAuhenticaionToken", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

}
