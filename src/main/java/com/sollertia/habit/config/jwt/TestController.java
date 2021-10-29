package com.sollertia.habit.config.jwt;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/user/login/test/{socialName}")
    public void loginTest(@RequestParam(value = "code") String authCode,
                          @Nullable @RequestParam(value = "state") String state,
                          @PathVariable String socialName) {
        System.out.println(state);
        System.out.println(socialName);
        System.out.println(authCode);
    }
}
