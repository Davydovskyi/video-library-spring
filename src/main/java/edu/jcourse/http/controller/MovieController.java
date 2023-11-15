package edu.jcourse.http.controller;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.PageResponse;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.service.MovieService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public String findAll(Model model, MovieFilter filter, Pageable pageable) {
        Page<MovieReadDto> movies = movieService.findAll(filter, pageable);
        model.addAttribute("movies", PageResponse.of(movies));
        model.addAttribute("genres", Genre.values());
        model.addAttribute("filter", filter);
        model.addAttribute("sorts", MovieFilter.Sort.values());
        return "movie/movies";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id,
                           Model model) {
        return movieService.findById(id)
                .map(movie -> {
                    model.addAttribute("movie", movie);
                    model.addAttribute("reviews", movie.reviews());
                    model.addAttribute("moviePersons", movie.moviePersons());
                    return "movie/movie";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/add")
    public String add(Model model,
                      @ModelAttribute("movie") MovieCreateEditDto movie) {
        model.addAttribute("movie", movie);
        model.addAttribute("genres", Genre.values());
        return "movie/add-movie";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({Default.class, CreateAction.class}) MovieCreateEditDto movie,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("movie", movie);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/movies/add";
        }
        movieService.create(movie);
        return "redirect:/movies";
    }

    @GetMapping("/{id}/pre-update")
    public String preUpdate(@PathVariable Integer id,
                            Model model) {
        return movieService.findById(id)
                .map(movie -> {
                    model.addAttribute("movie", movie);
                    model.addAttribute("genres", Genre.values());
                    return "movie/update-movie";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @ModelAttribute @Validated({Default.class, UpdateAction.class}) MovieCreateEditDto movie,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("movie", movie);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/movies/{id}/pre-update";
        }

        return movieService.update(id, movie)
                .map(it -> "redirect:/movies/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        if (!movieService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/movies";
    }
}