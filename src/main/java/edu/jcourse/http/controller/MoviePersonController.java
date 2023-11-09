package edu.jcourse.http.controller;

import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.service.MoviePersonService;
import edu.jcourse.service.PersonService;
import edu.jcourse.validation.group.CreateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie-person")
public class MoviePersonController {

    private final PersonService personService;
    private final MoviePersonService moviePersonService;

    @GetMapping("/add/{movieId}")
    public String add(@PathVariable Integer movieId,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        redirectAttributes.addFlashAttribute("errors", model.getAttribute("errors"));
        redirectAttributes.addFlashAttribute("showAddParticipant", true);
        redirectAttributes.addFlashAttribute("persons", personService.findAll());
        redirectAttributes.addFlashAttribute("movieRoles", PersonRole.values());

        return "redirect:/movies/{movieId}";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({Default.class, CreateAction.class}) MoviePersonCreateEditDto moviePerson,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        Integer movieId = moviePerson.movieId();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/movie-person/add/" + movieId;
        }

        moviePersonService.create(moviePerson);
        return "redirect:/movies/" + movieId;
    }
}