/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controllers;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.models.entity.User;
import com.example.demo.models.services.IUserService;
import com.example.demo.models.services.JwtUtilService;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ayosu
 */
@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private IUserService userRepository;
    @Autowired
    private JwtUtilService jwtUtilService;
    

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDto authRequestDto) {
   try {
       
       
       
        //1. Gestion authenticationManager
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.getUser(), authRequestDto.getPassword()
        ));

        //2. Validar el usuario en la bd
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequestDto.getUser());
         User userModel = userRepository.findByName(authRequestDto.getUser());


        //3. Generar token
        String jwt = this.jwtUtilService.generateToken(userDetails, userModel.getRole());
        
         AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setToken(jwt);

        return new ResponseEntity<>( authResponseDto ,HttpStatus.OK);
   }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error Authetication:::" + e.getMessage());
        }
      
    }
    
    
    
    

}
