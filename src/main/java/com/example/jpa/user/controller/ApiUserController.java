package com.example.jpa.user.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.exception.ExistEmailException;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.model.*;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
public class ApiUserController {
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;
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
   /* @PostMapping("/api/user_36")//36
    public ResponseEntity<?> addUser_36
            (@RequestBody @Valid UserInput userInput, Errors errors){
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
    return ResponseEntity.ok().build();}*/
    @ExceptionHandler(value = {ExistEmailException.class,
    PasswordNotMatchException.class})
    public ResponseEntity<?> ExistsEmailExceptionHandler(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);}

    @PatchMapping("/api/user/{id}/password")//37
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
    private String getEncryptPassword(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);}
    @PostMapping("/api/user_38")//38
    public ResponseEntity<?> addUser_38(@RequestBody @Valid UserInput userInput, Errors errors){
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e)->{
                responseErrorList.add(ResponseError.of((FieldError)e));});
            return new ResponseEntity<>(responseErrorList,HttpStatus.BAD_REQUEST);}
        if(userRepository.countByEmail(userInput.getEmail())>0){
            throw new ExistEmailException("이미 가입된 이메일O");}
        String encryptPassword=getEncryptPassword(userInput.getPassword());
        User user=User.builder().email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(userInput.getPassword())
                .regDate(LocalDateTime.now()).build();
        userRepository.save(user);
        return ResponseEntity.ok().build();}
    @DeleteMapping("/api/user/{id}") //39
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보X"));
        //내가 쓴 공지사항O: 1. 삭제 못해 ... 삭제 하려면 공지사항 삭제하고자
        // 2. 회원 삭제 전에 공지사항 글을 다 삭제하는 겨우..
        try{userRepository.delete(user);}
        catch (DataIntegrityViolationException e) {
            String message = "제약조건에 문제 발생";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            String message="회원 탈퇴 중 문제 발생";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);}
        return ResponseEntity.ok().build();}
    @GetMapping("/api/user")//40
    public ResponseEntity<?> findUser(@RequestBody UserInputFind userInputFind){
        User user=userRepository.findByUserNameAndPhone
                        (userInputFind.getUserName(),userInputFind.getPhone())
                .orElseThrow(()->new UserNotFoundException("사용자 정보X"));
        UserResponse userResponse=UserResponse.of(user);
        return ResponseEntity.ok().body(userResponse);}
    private String getResetPassword(){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,10);}
    @GetMapping("/api/user/{id}/password/reset")//41
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보X"));
        //pw 초기화
        String resetPassword=getEncryptPassword(getResetPassword());
        String resetEncryptPassword=getEncryptPassword(resetPassword);
        user.setPassword(resetEncryptPassword); userRepository.save(user);
        String message=String.format("[%s]님의 임시 pw가 [%s]로 초기화",
                user.getUserName(),resetPassword); sendSMS(message);
        return ResponseEntity.ok().build();}
    void sendSMS(String message){System.out.println("[문자 메세지 전송]");
        System.out.println(message);
    }public static void main(String[] args) {
        System.out.println(UUID.randomUUID());}
    @GetMapping("api/user/{id}/notice/like")//42
    public List<NoticeLike> likeNotice(@PathVariable Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보X"));
        List<NoticeLike> noticeLikeList=noticeLikeRepository.findByUser(user);
        return noticeLikeList;}
    //public static void main(String[] args) {
    //    System.out.println(LocalDateTime.now().plusMonths(1).toString());}
    @PostMapping("/api/user/login_43")//43
    public ResponseEntity<?> createToken_43
    (@RequestBody @Valid UserLogin userLogin, Errors errors){
        List<ResponseError> responseErrorList=new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e)->{
                responseErrorList.add(ResponseError.of((FieldError)e));});
            return new ResponseEntity<>(responseErrorList,HttpStatus.BAD_REQUEST);}
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보X"));
        if(!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("PW 불일치");}
            //return ResponseEntity.ok().build();}//44
        LocalDateTime expiredDateTime=LocalDateTime.now().plusMonths(1);//45
        Date expiredDate=java.sql.Timestamp.valueOf(expiredDateTime);
    String token= JWT.create().withExpiresAt(new Date())
            .withClaim("user_id", user.getId())
            .withSubject(user.getUserName())
            .withIssuer(user.getEmail())
            .sign(Algorithm.HMAC512("fastcampus".getBytes()));
return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());}
}