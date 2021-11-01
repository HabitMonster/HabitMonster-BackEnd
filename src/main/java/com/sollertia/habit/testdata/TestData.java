package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class TestData implements ApplicationRunner {

    private final UserRepository userRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        User testUser = User.builder().socialId("1234G").type(UserType.Google).build();
        userRepository.save(testUser);
    }
}
