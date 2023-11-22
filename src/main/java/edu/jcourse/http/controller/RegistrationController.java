package edu.jcourse.http.controller;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.user.UserCreateEditDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static edu.jcourse.util.HttpPath.REGISTRATION;

@Controller
public class RegistrationController {

    @GetMapping(REGISTRATION)
    public String registration(Model model,
                               @ModelAttribute("user") UserCreateEditDto user) {
        model.addAttribute("user", user);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("userRole", Role.USER);

        return "user/registration";
    }
}