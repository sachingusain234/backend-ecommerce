package com.neonriot.Spring_security_jwt.controller;

import com.neonriot.Spring_security_jwt.entity.User;
import com.neonriot.Spring_security_jwt.service.UserService;
import com.neonriot.Spring_security_jwt.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@CrossOrigin("https://soundverse-ruddy.vercel.app/")
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @GetMapping("health-check")
    public String checkHealth(){
        return "I am working fine";
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        //userService.saveNewUser(user);
        if(userService.usernameExists(user.getUserName())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        }catch(Exception e){
            log.error("Incorrect username found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
