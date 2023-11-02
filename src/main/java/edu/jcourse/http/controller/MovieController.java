package edu.jcourse.http.controller;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.PageResponse;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.service.MovieService;
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
    public String findById(@PathVariable Integer id, Model model) {
        return movieService.findById(id)
                .map(movie -> {
                    model.addAttribute("movie", movie);
                    model.addAttribute("reviews", movie.reviews());
                    model.addAttribute("moviePersons", movie.moviePersons());
                    return "movie/movie";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}