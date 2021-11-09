package com.sollertia.habit.domain.user.security.jwt.filter;

import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
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

    // 토큰 유효시간 설정 => now.getTime() 는 밀리 초 이기 때문에 ms -> s 변환이 필요해서 1000 곱함  1s = 1000ms
//    public static final long ACCESS_TOKEN_USETIME = 60 * 60 * 1000L; // 1시간
    //public static final long REFRESH_TOKEN_USETIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    //test User 용
    public static final long ACCESS_TOKEN_USETIME = 30 * 24 * 60 * 60 * 1000L;
    //public static final long ACCESS_TOKEN_USETIME = 1 * 1000L;
    public static final long REFRESH_TOKEN_USETIME = 500000 * 24 * 60 * 60 * 1000L;
    //public static final long REFRESH_TOKEN_USETIME = 1 * 1000L;

    // Refresh, Access 토큰 구분
    public String responseRefreshToken(User user) {
        return createToken(user.getSocialId(), user.getProviderType(), REFRESH_TOKEN_USETIME);
    }

    public String responseAccessToken(User user) {
        return createToken(user.getSocialId(), user.getProviderType(), ACCESS_TOKEN_USETIME);
    }

    // JWT 토큰 생성
    private String createToken(String socialId, ProviderType providerType, long useTime) {
        Claims claims = Jwts.claims().setSubject(socialId);
        claims.put("type", providerType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장 - socialId, userType
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + useTime)) // set ExpireTime
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }

    // token을 사용하여 UserDetails 생성 및 등록 준비
    public Authentication getAuthentication(String token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getSocialId(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (MalformedJwtException ex) {
            throw ex;
        }
    }

    // 토큰에서 socialId 추출
    public String getSocialId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Header 에서 access토큰 값 가져오기
    public String requestAccessToken(HttpServletRequest request) {
        return request.getHeader("A-AUTH-TOKEN");
    }


    // Header 에서 refresh토큰 값 가져오기
    public String requestRefreshToken(HttpServletRequest request) {
        return request.getHeader("R-AUTH-TOKEN");
    }

    // 토큰의 유효성 및 만료일자 확인
    public void validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException | SignatureException ex) {
            throw ex;
        }
    }
}
