package edu.jcourse.database.entity;

import lombok.Getter;

@Getter
public enum Genre {
    FANTASY("movie.genre.fantasy"),
    FICTION("movie.genre.fiction"),
    ACTION("movie.genre.action"),
    SCIENCE_FICTION("movie.genre.science_fiction"),
    THRILLER("movie.genre.thriller"),
    DRAMA("movie.genre.drama"),
    ROMANCE("movie.genre.romance"),
    ADVENTURE("movie.genre.adventure"),
    MUSICAL("movie.genre.musical");

    private final String code;

    Genre(String name) {
        this.code = name;
    }
}