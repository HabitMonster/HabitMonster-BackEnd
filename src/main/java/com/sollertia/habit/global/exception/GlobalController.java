package com.sollertia.habit.global.exception;


import com.sollertia.habit.global.exception.habit.HabitIdNotFoundException;
import com.sollertia.habit.global.exception.habit.HabitTypeNotFoundException;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import com.sollertia.habit.global.exception.monster.NotReachedMaximumLevelException;
import com.sollertia.habit.global.exception.user.InvalidSocialNameException;
import com.sollertia.habit.global.exception.user.NaverOauth2Exception;
import com.sollertia.habit.global.exception.user.OAuthProviderMissMatchException;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import com.sollertia.habit.global.utils.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> globalDateTimeParseExceptionHandler(DateTimeParseException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest("Date Type Error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> globalRedisConnectionExceptionHandler(RedisConnectionException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> globalJwtExceptionHandler(JwtException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> globalNullPointerExceptionHandler(NullPointerException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest("요청에 해당하는 값이 없습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> globalIllegalArgumentExceptionHandler(IllegalArgumentException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest("잘못된 요청 입니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> oAuthProviderMissMatchExceptionHandler(OAuthProviderMissMatchException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> invalidSocialNameExceptionHandler(InvalidSocialNameException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> userIdNotFoundExceptionHandler(UserIdNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDto.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> naverOauth2ExceptionHandler(NaverOauth2Exception exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> monsterNotFoundExceptionHandler(MonsterNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDto.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> HabitIdNotFoundExceptionHandler(HabitIdNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDto.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> HabitTypeNotFoundExceptionHandler(HabitTypeNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDto.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> notReachedMaximumLevelExceptionHandler(NotReachedMaximumLevelException exception) {
        return new ResponseEntity<>(ErrorResponseDto.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
