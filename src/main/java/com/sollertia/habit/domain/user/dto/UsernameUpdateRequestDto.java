package com.sollertia.habit.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UsernameUpdateRequestDto {
    @NotBlank(message = "값이 비어있지 않아야 합니다.")
    @Size(max = 10, message = "닉네임은 최대 10자입니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9]*$",message = "닉네임은 알파벳대소문자, 한글, 숫자만 가능합니다.")
    private String username;
}
