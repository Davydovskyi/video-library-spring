package edu.jcourse.http.controller;

import edu.jcourse.dto.PageResponse;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.service.PersonService;
import edu.jcourse.validation.group.CreateAction;
import edu.jcourse.validation.group.UpdateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static edu.jcourse.util.HttpPath.*;

@Controller
@RequestMapping(PERSONS)
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public String findAll(Model model, PersonFilter filter, Pageable pageable) {
        Page<PersonReadDto> persons = personService.findAll(filter, pageable);
        PageResponse<PersonReadDto> response = PageResponse.of(persons);
        model.addAttribute("persons", response.content());
        model.addAttribute("metaData", response.metaData());
        model.addAttribute("filter", filter);
        model.addAttribute("sorts", PersonFilter.Sort.values());
        return "person/persons";
    }

    @GetMapping(PERSON_BY_ID)
    public String findById(@PathVariable Integer id,
                           Model model) {
        return personService.findById(id)
                .map(person -> {
                    model.addAttribute("person", person);
                    model.addAttribute("movies", getMovies(person));
                    return "person/person";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(PERSON_ADD)
    public String add(Model model,
                      @ModelAttribute("person") PersonCreateEditDto person) {
        model.addAttribute("person", person);
        return "person/add-person";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({CreateAction.class, Default.class}) PersonCreateEditDto person,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("person", person);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/persons/add";
        }

        personService.create(person);
        return "redirect:/persons";
    }

    @PostMapping(PERSON_UPDATE)
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated({UpdateAction.class, Default.class}) PersonCreateEditDto person,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("person", person);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/persons/{id}";
        }

        return personService.update(id, person)
                .map(it -> "redirect:/persons/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(PERSON_DELETE)
    public String delete(@PathVariable Integer id) {
        if (!personService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/persons";
    }

    private List<MovieReadDto> getMovies(PersonReadDto person) {
        return person.moviePersons().stream()
                .map(MoviePersonReadDto::movie)
                .distinct()
                .toList();
    }
}