package com.sollertia.habit.config.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.config.jwt.dto.JwtExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            String clientRequestUri = (String) request.getAttribute("clientRequestUri");
            String message = (String) request.getAttribute("msg");
            String body = (String) request.getAttribute("messageBody");
            Map<String, Object> bodyM = null;
            if(!body.isEmpty()){
                ObjectMapper objectMapper = new ObjectMapper();
                bodyM = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
            }
            responseExceptionMsg(response, clientRequestUri, message, bodyM);
        }

    }

    private void responseExceptionMsg(HttpServletResponse response, String clientRequestUri, String msg, Map<String, Object> body) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        ResponseEntity<JwtExceptionDto> errorResponse =
                ResponseEntity.ok(JwtExceptionDto.builder().responseMessage(msg).statusCode(400).clientRequestUri(clientRequestUri).body(body).build());
        try {
            String json = objectMapper.writeValueAsString(errorResponse.getBody());
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
