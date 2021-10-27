package com.sollertia.habit.config.jwt;

import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.utils.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final  RedisUtil redisUtil;

    private final  UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.requestAccessToken(request);

        String refreshToken = null;
        String refreshUserId;

        if (jwtToken != null) {
            if (jwtTokenProvider.validateToken(jwtToken)) {

                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {

                refreshToken = jwtTokenProvider.requestRefreshToken(request);

            }
        } else {
            refreshToken = jwtTokenProvider.requestRefreshToken(request);
        }

        if (refreshToken != null) {
            if (jwtTokenProvider.validateToken(refreshToken)) {

                refreshUserId = redisUtil.getData(refreshToken);

                if (refreshUserId.equals(jwtTokenProvider.getUserId(refreshToken))) {

                    Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    User user = userRepository.findByUserId(refreshUserId)
                            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

                    String newAccessToken = jwtTokenProvider.responseAccessToken(user);

                    response.addHeader("A-AUTH-TOKEN", newAccessToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}