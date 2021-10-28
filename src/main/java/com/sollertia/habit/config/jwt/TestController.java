package com.sollertia.habit.config.jwt;

import com.sollertia.habit.config.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import com.sollertia.habit.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final UserRepository userRepository;

    private final JwtHandler jwtHandler;

    private final RedisUtil redisUtil;



    @PostMapping("/get/token")
    public JwtResponseDto getToken(){
        String accessToken = null;
        String refreshToken = null;

        Optional<User> user = userRepository.findByUserId("username");
        user.get().setType(UserType.Google);
        if(user.isPresent()){
           accessToken = jwtHandler.getAccessToken(user.get());
           refreshToken =jwtHandler.getRefreshToken(user.get());
           redisUtil.setDataExpire(refreshToken,user.get().getUserId(),JwtTokenProvider.REFRESH_TOKEN_USETIME/1000L);
        }

        return JwtResponseDto.builder().accesstoken(accessToken).refreshtoken(refreshToken).isFirstLongin(false).build();


    }

}
