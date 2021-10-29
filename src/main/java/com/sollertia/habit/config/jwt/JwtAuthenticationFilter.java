package com.sollertia.habit.config.jwt;

import com.sollertia.habit.utils.RedisUtil;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisUtil redisUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.requestAccessToken(request);
        String refreshToken = jwtTokenProvider.requestRefreshToken(request);
        String refreshUserId = null;

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        if (jwtToken != null) {

            try {

                checkToken(jwtToken);

            } catch (ExpiredJwtException ex) {
                createRequest(request, "accessToken 만료", request.getRequestURI(), messageBody);
                throw ex;
            } catch (SignatureException ex) {
                createRequest(request, "accessToken 인증 오류", request.getRequestURI(), messageBody);
                throw ex;
            } catch (MalformedJwtException ex) {
                createRequest(request, "accessToken 손상", request.getRequestURI(), messageBody);
                throw ex;
            } catch (UnsupportedJwtException ex) {
                createRequest(request, "accessToken 지원불가", request.getRequestURI(), messageBody);
                throw ex;
            }

        } else if (refreshToken != null) {

            try {

                try {
                    refreshUserId = redisUtil.getData(refreshToken);
                } catch (Exception ex) {
                    createRequest(request, "Redis 연결에 문제가 있습니다.", request.getRequestURI(), messageBody);
                    throw ex;
                }

                if (!refreshUserId.equals(jwtTokenProvider.getUserId(refreshToken))) {
                    createRequest(request, "RefreshToken 탈취 가능성이 있습니다. RefreshToken을 새롭게 발급 받으세요.", request.getRequestURI(), messageBody);
                    throw new JwtException("");
                }

                checkToken(refreshToken);

            } catch (ExpiredJwtException ex) {
                createRequest(request, "refreshToken 만료", request.getRequestURI(), messageBody);
                throw ex;
            } catch (SignatureException ex) {
                createRequest(request, "refreshToken 인증 오류", request.getRequestURI(), messageBody);
                throw ex;
            } catch (MalformedJwtException ex) {
                createRequest(request, "refreshToken 손상", request.getRequestURI(), messageBody);
                throw ex;
            } catch (UnsupportedJwtException ex) {
                createRequest(request, "refreshToken 지원불가", request.getRequestURI(), messageBody);
                throw ex;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void createRequest(HttpServletRequest request, String message, String clientRequestUri, String messageBody) {
        request.setAttribute("msg", message);
        request.setAttribute("clientRequestUri", clientRequestUri);
        request.setAttribute("messageBody", messageBody);
    }

    private void checkToken(String token) {
        jwtTokenProvider.validateToken(token);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}