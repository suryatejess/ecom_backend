package com.example.ecom_backend.controllers;

import com.example.ecom_backend.dtos.UserSignUpDTO;
import com.example.ecom_backend.entities.AppUser;
import com.example.ecom_backend.repositories.UserRepo;
import com.example.ecom_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;

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

}
