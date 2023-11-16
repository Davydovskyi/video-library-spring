package edu.jcourse.http.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "edu.jcourse.http.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception exception) {
        model.addAttribute("message", exception.getMessage());
        return "error/error500";
    }
}