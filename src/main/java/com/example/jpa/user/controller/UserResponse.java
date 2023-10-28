package com.example.jpa.user.controller;
import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class UserResponse {
    private long id;
    private String email;
    private String userName;
    protected String phone;
    /*public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.phone = user.getPhone();}*/
    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail()).build();}
}