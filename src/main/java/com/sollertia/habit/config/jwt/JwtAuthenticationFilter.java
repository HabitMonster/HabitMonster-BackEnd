package com.sollertia.habit.config.jwt;

import com.sollertia.habit.utils.RedisUtil;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.requestAccessToken(request);
        String refreshToken = jwtTokenProvider.requestRefreshToken(request);
        String refreshUserId = null;

        if (jwtToken != null) {

            try {

                checkToken(jwtToken);

            } catch (ExpiredJwtException ex) {
                createRequest(request, "accessToken 만료", request.getRequestURI());
                throw ex;
            } catch (SignatureException ex) {
                createRequest(request, "accessToken 인증 오류", request.getRequestURI());
                throw ex;
            } catch (MalformedJwtException ex) {
                createRequest(request, "accessToken 손상", request.getRequestURI());
                throw ex;
            } catch (UnsupportedJwtException ex) {
                createRequest(request, "accessToken 지원불가", request.getRequestURI());
                throw ex;
            }

        } else if (refreshToken != null) {

            try {

                try {
                    refreshUserId = redisUtil.getData(refreshToken);
                } catch (Exception ex) {
                    createRequest(request, "Redis 연결에 문제가 있습니다.", request.getRequestURI());
                    throw ex;
                }

                if (!refreshUserId.equals(jwtTokenProvider.getUserId(refreshToken))) {
                    createRequest(request, "RefreshToken 탈취 가능성이 있습니다. RefreshToken을 새롭게 발급 받으세요.", request.getRequestURI());
                    throw new JwtException("");
                }

                checkToken(refreshToken);

            } catch (ExpiredJwtException ex) {
                createRequest(request, "refreshToken 만료", request.getRequestURI());
                throw ex;
            } catch (SignatureException ex) {
                createRequest(request, "refreshToken 인증 오류", request.getRequestURI());
                throw ex;
            } catch (MalformedJwtException ex) {
                createRequest(request, "refreshToken 손상", request.getRequestURI());
                throw ex;
            } catch (UnsupportedJwtException ex) {
                createRequest(request, "refreshToken 지원불가", request.getRequestURI());
                throw ex;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void createRequest(HttpServletRequest request, String message, String clientRequestUrl) {
        request.setAttribute("msg", message);
        request.setAttribute("clientRequestUrl", clientRequestUrl);
    }

    private void checkToken(String token) {
        jwtTokenProvider.validateToken(token);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}