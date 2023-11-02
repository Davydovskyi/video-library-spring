package edu.jcourse.database.entity;

import lombok.Getter;

@Getter
public enum PersonRole {
    PRODUCER("personRole.producer"),
    DIRECTOR("personRole.director"),
    ACTOR("personRole.actor"),
    COMPOSER("personRole.composer");

    private final String code;

    PersonRole(String name) {
        this.code = name;
    }
}