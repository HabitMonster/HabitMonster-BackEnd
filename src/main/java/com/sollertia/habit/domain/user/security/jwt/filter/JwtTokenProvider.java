package com.sollertia.habit.domain.user.security.jwt.filter;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.ProviderType;
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


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public static final long ACCESS_TOKEN_USETIME = 2 * 60 * 60 * 1000L;

    public static final long REFRESH_TOKEN_USETIME = 5 * 24 * 60 * 60 * 1000L;

    public String responseRefreshToken(User user) {
        return createToken(user.getSocialId(), user.getProviderType(), REFRESH_TOKEN_USETIME);
    }

    public String responseAccessToken(User user) {
        return createToken(user.getSocialId(), user.getProviderType(), ACCESS_TOKEN_USETIME);
    }


    private String createToken(String socialId, ProviderType providerType, long useTime) {
        Claims claims = Jwts.claims().setSubject(socialId);
        claims.put("type", providerType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + useTime))
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getSocialId(token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (MalformedJwtException ex) {
            throw ex;
        }
    }


    public String getSocialId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String requestAccessToken(HttpServletRequest request) {
        return request.getHeader("A-AUTH-TOKEN");
    }



    public String requestRefreshToken(HttpServletRequest request) {
        return request.getHeader("R-AUTH-TOKEN");
    }


    public void validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        } catch (ExpiredJwtException | SignatureException ex) {
            throw ex;
        }
    }
}
