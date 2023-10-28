package com.example.jpa.user.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class UserInputPassword {
    @NotBlank(message = "현재 pw:필수")
    private String password;
    @Size(min = 4, message = "신규 비밀번호는 4자 이상 입력해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    private String newPassword;
}