package com.sollertia.habit.config.jwt;

import com.sollertia.habit.domain.user.UserDetailsServiceImpl;
import com.sollertia.habit.domain.user.UserType;
import com.sollertia.habit.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret.key}")
    private String secretKey;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct // bean 이 여러번 초기화되는 상황 방지용
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 유효시간 설정 cf) now.getTime() 는 밀리 초 이기 때문에 ms -> s 변환이 필요해서 1000 곱함  1s = 1000ms
    protected static final long ACCESS_TOKEN_USETIME = 30 * 60 * 1000L; //30분
    protected static final long REFRESH_TOKEN_USETIME = 7 * 24 * 60 * 60 * 1000L; //7일

    protected static final String ACCESS_TOKEN = "accessToken";
    protected static final String REFRESH_TOKEN = "refreshToken";

    // Refresh, Access 토큰 구분
    public String getRefreshToken(User user){
        return createToken(user.getUserId(), user.getType(), REFRESH_TOKEN_USETIME);
    }

    public String getAccessToken(User user){
        return createToken(user.getUserId(), user.getType(), ACCESS_TOKEN_USETIME);
    }

    // JWT 토큰 생성
    private String createToken(String userId, UserType userType, long useTime) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("type", userType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장 - userId, userType
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + useTime)) // set ExpireTime
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }

    // token을 사용하여 UserDetails 생성
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserInfo(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    private String getUserInfo(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // 토큰에서 userId 추출
    }

    // Header 에서 토큰 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("H-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
