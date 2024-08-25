package com.gpact.bank_app.controller;

import com.gpact.bank_app.dto.AuthResponseDto;
import com.gpact.bank_app.dto.AuthRequestDto;
import com.gpact.bank_app.model.UserMaster;
import com.gpact.bank_app.service.JwtService;
import com.gpact.bank_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateAndGetToken(@RequestBody AuthRequestDto authRequest) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        System.out.printf("authentication>>> "+ authentication);
        if (authentication.isAuthenticated()) {
            authResponseDto.setAuthToken(jwtService.generateToken(authRequest.getUsername()));
            return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
        }else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createUser")
    public ResponseEntity<Object> createUser(@RequestBody UserMaster userRequest){
        UserMaster userMaster = new UserMaster();
        userMaster.setUsername(userRequest.getUsername());
        userMaster.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userMaster.setAge(userRequest.getAge());
        userMaster.setEmail(userRequest.getEmail());
        userMaster.setRoles(userRequest.getRoles());
        return new ResponseEntity<>(userService.createUser(userMaster),HttpStatus.CREATED);
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<Object> createAdmin(){
        List<UserMaster> userMasterList = new ArrayList<>();
        UserMaster userMaster = new UserMaster();
        userMaster.setUsername("usr1");
        userMaster.setPassword(passwordEncoder.encode("pwd1"));
        userMaster.setAge(28);
        userMaster.setEmail("usr1@mail.com");
        userMaster.setRoles("ROLE_ADMIN");
        userMasterList.add(userMaster);

        UserMaster userMaster2 = new UserMaster();
        userMaster2.setUsername("usr2");
        userMaster2.setPassword(passwordEncoder.encode("pwd2"));
        userMaster2.setAge(30);
        userMaster2.setEmail("usr2@mail.com");
        userMaster2.setRoles("ROLE_USER");
        userMasterList.add(userMaster2);

        UserMaster userMaster3 = new UserMaster();
        userMaster3.setUsername("usr3");
        userMaster3.setPassword(passwordEncoder.encode("pwd3"));
        userMaster3.setAge(32);
        userMaster3.setEmail("usr3@mail.com");
        userMaster3.setRoles("ROLE_ADMIN, ROLE_USER");
        userMasterList.add(userMaster3);

        return new ResponseEntity<>(userService.createAdmin(userMasterList),HttpStatus.CREATED);
    }
}
