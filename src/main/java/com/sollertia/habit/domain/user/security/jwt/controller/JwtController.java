package com.sollertia.habit.domain.user.security.jwt.controller;


import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.domain.user.security.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @GetMapping("/user/loginCheck")
    public ResponseEntity<JwtResponseDto> loginCheck(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.requestRefreshToken(request);

        if (refreshToken != null) {
            Optional<User> user = userRepository.findBySocialId(jwtTokenProvider.getSocialId(refreshToken));
            if (user.isPresent()) {
                String accessToken = jwtTokenProvider.responseAccessToken(user.get());
                return ResponseEntity.ok().body(JwtResponseDto.builder()
                        .responseMessage("Issuance completed accessToken")
                        .statusCode(200)
                        .accessToken(accessToken)
                        .isFirstLogin(false)
                        .build());
            } else {
                throw new UserIdNotFoundException("NotFound User");
            }
        } else {
            throw new JwtException("NotFound RefreshToken");
        }

    }

    @GetMapping("/user/check")
    public ResponseEntity<JwtResponseDto> userCheck(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.requestAccessToken(request);

        if (accessToken != null) {
            return ResponseEntity.ok().body(JwtResponseDto.builder().responseMessage("IsLogin True").
                    statusCode(200).isFirstLogin(false).isLogin(true).build());
        } else {
            throw new JwtException("NotFound AccessToken");
        }
    }

}
