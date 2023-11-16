package edu.jcourse.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "edu.jcourse.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}