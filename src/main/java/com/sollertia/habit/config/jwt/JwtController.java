package com.sollertia.habit.config.jwt;

import com.sollertia.habit.config.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 체크할 시 만료기간 확인하고 access 토큰 재발급, 만료 되었다면 실패 메세지 -> 클라이언트에서 소셜로그인으로 유도 해주면
    // 유저가 다시 소셜 로그인 하면 새롭게 access, refresh 토큰 발급
    @GetMapping("/user/logincheck")
    public ResponseEntity loginCheck(HttpServletRequest request, HttpServletResponse response,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails != null) {
            String jwtToken = jwtTokenProvider.requestAccessToken(request);
            if(jwtToken!=null){
                return ResponseEntity.ok().body(JwtResponseDto.builder().accesstoken(jwtToken).isFirstLongin(false).build());
            }
        }

        Map<String,String> msg = new HashMap<>();
        msg.put("message","fail");
        return ResponseEntity.ok().body(msg);

    }
}
