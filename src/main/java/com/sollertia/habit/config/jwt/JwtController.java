package com.sollertia.habit.config.jwt;

import com.sollertia.habit.config.jwt.dto.JwtRequestDto;
import com.sollertia.habit.config.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.utils.RedisUtil;
import io.jsonwebtoken.*;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisUtil redisUtil;

    private final UserRepository userRepository;

    // 로그인 체크할 시 refreshToken 만료기간,유효성 확인하고 accessToken 재발급, 만료 되었다면 실패 메세지 -> 클라이언트에서 유저를 소셜로그인으로 유도
    // 유저가 다시 소셜 로그인 하면 새롭게 access, refresh 토큰 발급
    @PostMapping("/user/loginCheck")
    public ResponseEntity<JwtResponseDto> loginCheck(@RequestBody JwtRequestDto requestDto) {

        String refreshToken = requestDto.getRefreshToken();
        String refreshUserId;

        if (refreshToken != null) {
            try {
                try {
                    refreshUserId = redisUtil.getData(refreshToken);
                } catch (Exception ex) {
                    throw new RedisConnectionException("Redis 연결에 문제가 있습니다.");
                }

                if (!refreshUserId.equals(jwtTokenProvider.getSocialId(refreshToken))) {
                    throw new JwtException("RefreshToken 탈취 가능성이 있습니다. RefreshToken을 새롭게 발급 받으세요.");
                }

                jwtTokenProvider.validateToken(refreshToken);
                jwtTokenProvider.getAuthentication(refreshToken);

                Optional<User> user = userRepository.findBySocialId(jwtTokenProvider.getSocialId(refreshToken));
                if (user.isPresent()) {
                    String accessToken = jwtTokenProvider.responseAccessToken(user.get());
                    return ResponseEntity.ok().body(JwtResponseDto.builder().responseMessage("accessToken 발급완료!").statusCode(200).accessToken(accessToken).build());
                } else {
                    throw new IllegalArgumentException("유저가 존재하지 않습니다.");
                }

            } catch (ExpiredJwtException ex) {
                throw new JwtException("refreshToken 만료");
            } catch (SignatureException ex) {
                throw new JwtException("refreshToken 인증 오류");
            } catch (MalformedJwtException ex) {
                throw new JwtException("refreshToken 손상");
            } catch (UnsupportedJwtException ex) {
                throw new JwtException("refreshToken 지원불가");
            }
        } else {
            throw new NullPointerException("RefreshToken 분실");
        }
    }
}
