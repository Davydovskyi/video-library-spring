package edu.jcourse.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpPath {
    public static final String LOGIN = "/login";
    public static final String REGISTRATION = "/registration";
    public static final String LOGOUT = "/logout";
    public static final String MOVIES = "/movies";
    public static final String MOVIE_BY_ID = "/{id}";
    public static final String MOVIE_PRE_UPDATE = "/{id}/pre-update";
    public static final String MOVIE_UPDATE = "/{id}/update";
    public static final String MOVIE_DELETE = "/{id}/delete";
    public static final String MOVIE_ADD = "/add";
    public static final String MOVIE_PERSONS = "/movie-persons";
    public static final String MOVIE_PERSONS_ADD = "/add/{movieId}";
    public static final String PERSONS = "/persons";
    public static final String PERSON_BY_ID = "/{id}";
    public static final String PERSON_ADD = "/add";
    public static final String PERSON_UPDATE = "/{id}/update";
    public static final String PERSON_DELETE = "/{id}/delete";
    public static final String REVIEWS = "/reviews";
    public static final String REVIEWS_BY_USER_ID = "/{userId}";
    public static final String REVIEW_ADD = "/add/{movieId}";
    public static final String USERS = "/users";
    public static final String USER_BY_ID = "/{id}";
    public static final String USER_UPDATE = "/{id}/update";
    public static final String USER_DELETE = "/{id}/delete";
    public static final String REST_USERS = "/api/v1/users";
    public static final String USER_AVATAR = "/{id}/avatar";
    public static final String REST_PERSONS = "/api/v1/persons";
    public static final String REST_MOVIES = "/api/v1/movies";
}