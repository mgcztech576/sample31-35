package com.example.jpa.user.controller;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.exception.ExistEmailException;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.model.UserInput;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.user.model.UserInputPassword;
import com.example.jpa.user.model.UserUpdate;
import com.example.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RestController
public class ApiUserController {
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    /*@PostMapping("/api/user_31")//31,32
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
        return ResponseEntity.ok().build();}*/
    @PutMapping("/api/user/{id}")
    public HttpEntity<List<ResponseError>> updateUser//33
    (@PathVariable Long id, @RequestBody @Valid UserUpdate userUpdate, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));});
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);}
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자 정보X"));
        user.setPhone(userUpdate.getPhone());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok().build();}
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);}
    @GetMapping("/api/user/{id}")//34
    public UserResponse getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보X"));
        //UserResponse userResponse = new UserResponse(user);
        UserResponse userResponse = UserResponse.of(user);
        return userResponse;}
    @GetMapping("/api/user/{id}/notice")//35
    public List<NoticeResponse> userNotice(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보X"));
        List<Notice> noticeList= noticeRepository.findByUser(user);
        List<NoticeResponse> noticeResponseList=new ArrayList<>();
        noticeList.stream().forEach((e)->{
            noticeResponseList.add(NoticeResponse.of(e));});
        return noticeResponseList;}
    @PostMapping("/api/user")//36
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors){
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e)->{
                responseErrorList.add(ResponseError.of((FieldError)e));});
            return new ResponseEntity<>(responseErrorList,HttpStatus.BAD_REQUEST);}
        if(userRepository.countByEmail(userInput.getEmail())>0){
        throw new ExistEmailException("이미 가입된 이메일O");}
       // User existUser= userRepository.findByEmail(userInput.getEmail())오류
       //         .orElseThrow(()->new ExistEmailException("이미 가입된 이메일O"));
        User user=User.builder().email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(userInput.getPassword())
                .regDate(LocalDateTime.now()).build();
    userRepository.save(user);
    return ResponseEntity.ok().build();}
    @ExceptionHandler(ExistEmailException.class)//PassWordNotMathException.class})
    public ResponseEntity<?> ExistEmailExceptionHandler(ExistEmailException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);}
    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id,
UserInputPassword userInputPassword,Errors errors){
        List<ResponseError> responseErrorList=new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e)->{
                responseErrorList.add(ResponseError.of((FieldError)e));});
            return new ResponseEntity<>(responseErrorList,HttpStatus.BAD_REQUEST);}
        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));
        user.setPassword(userInputPassword.getNewPassword());
        userRepository.save(user); return ResponseEntity.ok().build();}
}