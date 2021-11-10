package com.sollertia.habit.domain.user.security.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JwtExceptionHandlerFilterTest {

    HttpServletResponse response = null;
    HttpServletRequest request = null;
    FilterChain filterChain = null;
    PrintWriter mockWriter = null;

    @BeforeEach
    private void setUp() throws IOException {
        //given
        response = mock(HttpServletResponse.class);
        request = mock(HttpServletRequest.class);
        filterChain = mock(FilterChain.class);
        mockWriter = mock(PrintWriter.class);

        String clientRequestUri = "/test";
        String message = "test message";
        String body = null;
        String method = "GET";
        int code = 400;

        given(request.getAttribute("clientRequestUri")).willReturn(clientRequestUri);
        given(request.getAttribute("msg")).willReturn(message);
        given(request.getAttribute("messageBody")).willReturn(body);
        given(request.getAttribute("method")).willReturn(method);
        given(request.getAttribute("code")).willReturn(code);
        given(response.getWriter()).willReturn(mockWriter);
    }

    @DisplayName("Jwt ExpiredJwtException 처리")
    @Test
    void filterExpiredJwtException() throws ServletException, IOException {
        willThrow(ExpiredJwtException.class).given(filterChain).doFilter(request, response);

        //then
        JwtExceptionHandlerFilter jwtExceptionHandlerFilter = new JwtExceptionHandlerFilter();
        jwtExceptionHandlerFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response.getWriter()).write(anyString());
    }

    @DisplayName("Jwt SignatureException 처리")
    @Test
    void filterSignatureException() throws ServletException, IOException {
        willThrow(SignatureException.class).given(filterChain).doFilter(request, response);

        //then
        JwtExceptionHandlerFilter jwtExceptionHandlerFilter = new JwtExceptionHandlerFilter();
        jwtExceptionHandlerFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response.getWriter()).write(anyString());
    }

    @DisplayName("Jwt MalformedJwtException 처리")
    @Test
    void filterMalformedJwtException() throws ServletException, IOException {
        willThrow(MalformedJwtException.class).given(filterChain).doFilter(request, response);

        //then
        JwtExceptionHandlerFilter jwtExceptionHandlerFilter = new JwtExceptionHandlerFilter();
        jwtExceptionHandlerFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response.getWriter()).write(anyString());
    }
}