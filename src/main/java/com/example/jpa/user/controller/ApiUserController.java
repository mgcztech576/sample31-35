package com.example.jpa.user.controller;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.UserInput;
import com.example.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RestController
public class ApiUserController {
    private final UserRepository userRepository;
//return new ResponseEntity<>(HttpStatus.OK);
@PostMapping("/api/user_31")//31,32
public ResponseEntity<?> addUser_31(@RequestBody @Valid UserInput userInput, Errors errors) {
    List<ResponseError> responseErrorList = new ArrayList<>();
    if (errors.hasErrors()) {
        errors.getAllErrors().forEach((e) -> {
            responseErrorList.add(ResponseError.of((FieldError) e));});
        return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);}
    //return new ResponseEntity<>(HttpStatus.OK);
    User user = User.builder()//32
            .email(userInput.getEmail())
            .userName(userInput.getUserName())
            .password(userInput.getPassword())
            .phone(userInput.getPhone())
            .regDate(LocalDateTime.now()).build();
    userRepository.save(user);
    return ResponseEntity.ok().build();
}}