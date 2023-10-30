package com.example.jpa.user.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @NotBlank(message = "이메일 항목:필수")
    public String email;
    @NotBlank(message = "PW 항목: 필수")
    public String password;
}
