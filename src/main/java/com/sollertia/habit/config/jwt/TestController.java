package com.sollertia.habit.config.jwt;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("/")
    public String login() {
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/user/login/test/{socialName}")
    public void loginTest(@RequestParam(value = "code") String authCode,
                          @Nullable @RequestParam(value = "state") String state,
                          @PathVariable String socialName) {
        System.out.println(state);
        System.out.println(socialName);
        System.out.println(authCode);
    }
}
