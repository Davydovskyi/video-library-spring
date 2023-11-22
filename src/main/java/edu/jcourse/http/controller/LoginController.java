package edu.jcourse.http.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static edu.jcourse.util.HttpPath.LOGIN;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping(LOGIN)
    public String login() {
        return "user/login";
    }
}