package edu.jcourse.http.controller;

import edu.jcourse.dto.PageResponse;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public String findAll(Model model, PersonFilter filter, Pageable pageable) {
        Page<PersonReadDto> persons = personService.findAll(filter, pageable);
        model.addAttribute("persons", PageResponse.of(persons));
        model.addAttribute("filter", filter);
        model.addAttribute("sorts", PersonFilter.Sort.values());
        return "person/persons";
    }

    @GetMapping("/{id}")
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

    private List<MovieReadDto> getMovies(PersonReadDto person) {
        return person.moviePersons().stream()
                .map(MoviePersonReadDto::movie)
                .distinct()
                .toList();
    }
}
