package com.sollertia.habit.config.jwt;

import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestUserData implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 테스트 유저 생성
        User testUser = new User();
        testUser.setUserId("username");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setType(UserType.Google);
        userRepository.save(testUser);
    }
}
