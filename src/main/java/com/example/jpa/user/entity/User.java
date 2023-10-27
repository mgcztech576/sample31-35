package com.example.jpa.user.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder @Data @Entity
public class User {@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column private String email;
    @Column private String userName;
    @Column private String password;
    @Column private String phone;
    @Column private LocalDateTime regDate;
}