package com.sollertia.habit.domain.user.security.jwt.filter;


import com.sollertia.habit.global.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
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
        String method = request.getMethod();
        String refreshSocialId;
        String messageBody;

        if (jwtToken != null) {
            try {

                checkToken(jwtToken);

            } catch (ExpiredJwtException ex) {
                messageBody = getBody(request);
                createRequest(request, "accessToken Expired", request.getRequestURI(), messageBody, method, 400);
                throw ex;
            } catch (SignatureException ex) {
                messageBody = getBody(request);
                createRequest(request, "accessToken SignatureException", request.getRequestURI(), messageBody, method, 401);
                throw ex;
            } catch (MalformedJwtException ex) {
                messageBody = getBody(request);
                createRequest(request, "accessToken Malformed", request.getRequestURI(), messageBody, method, 401);
                throw ex;
            }

        } else if (refreshToken != null) {

            try {

                try {

                    refreshSocialId = redisUtil.getData(refreshToken);

                } catch (Exception ex) {
                    messageBody = getBody(request);
                    createRequest(request, "UnConnected Redis", request.getRequestURI(), messageBody, method, 401);
                    throw ex;
                }

                if (!refreshSocialId.equals(jwtTokenProvider.getSocialId(refreshToken))) {
                    messageBody = getBody(request);
                    createRequest(request, "refreshToken Error! Please Issuance New Token", request.getRequestURI(), messageBody, method, 401);
                    throw new JwtException("");
                }

                checkToken(refreshToken);

            } catch (ExpiredJwtException ex) {
                messageBody = getBody(request);
                createRequest(request, "refreshToken Expired", request.getRequestURI(), messageBody, method, 400);
                throw ex;
            } catch (SignatureException ex) {
                messageBody = getBody(request);
                createRequest(request, "refreshToken SignatureException", request.getRequestURI(), messageBody, method, 401);
                throw ex;
            } catch (MalformedJwtException ex) {
                messageBody = getBody(request);
                createRequest(request, "refreshToken Malformed", request.getRequestURI(), messageBody, method, 401);
                throw ex;
            }
        }
        filterChain.doFilter(request,response);
}
    private void createRequest(HttpServletRequest request, String message, String clientRequestUri, String messageBody, String method, int code) {
        request.setAttribute("msg", message);
        request.setAttribute("clientRequestUri", clientRequestUri);
        request.setAttribute("messageBody", messageBody);
        request.setAttribute("method",method);
        request.setAttribute("code",code);
    }

    private void checkToken(String token) {
        jwtTokenProvider.validateToken(token);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getBody(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    }
}