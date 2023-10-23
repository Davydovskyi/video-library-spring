package edu.jcourse.database.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> {

    K getId();

    void setId(K id);
}