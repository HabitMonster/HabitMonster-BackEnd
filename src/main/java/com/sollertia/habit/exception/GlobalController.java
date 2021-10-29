package com.sollertia.habit.exception;

import com.sollertia.habit.utils.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalController {

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
    public ResponseEntity<ErrorResponseDto> avatarNotFoundExceptionHandler(AvatarNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDto.notFound(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
