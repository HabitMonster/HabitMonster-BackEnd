package com.sollertia.habit.config;

import com.sollertia.habit.config.jwt.JwtAuthenticationFilter;
import com.sollertia.habit.config.jwt.JwtExceptionHandlerFilter;
import com.sollertia.habit.config.jwt.JwtTokenProvider;
import com.sollertia.habit.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeRequests()
//                .anyRequest().permitAll()
                .antMatchers("/user/login/**").anonymous()
                .antMatchers("/user/loginCheck").anonymous()
                .antMatchers("/test/**").anonymous()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,redisUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionHandlerFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**",
                        "/v2/**",
                        "/webjars/**",
                        "/swagger**",
                        "/swagger-resources/**",
                        "/member/**",
                        "/resources/**");
    }
}
