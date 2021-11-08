package com.sollertia.habit.domain.user.security.jwt.filter;

import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class JwtExceptionHandlerFilterTest {

    @InjectMocks
    private JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisUtil redisUtil;
    @Mock
    FilterChain filterChain;

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockFilterChain chain = new MockFilterChain();

    @DisplayName("JwtExcepion 처리")
    @Test
    void filterException() throws ServletException, IOException {

        //given
        //willThrow(RuntimeException.class).given(chain).doFilter(request,response);

        //then
        jwtExceptionHandlerFilter.doFilterInternal(request,response,chain);

    }

}