package com.sollertia.habit.testdata;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test")
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
