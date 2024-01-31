package edu.jcourse.dto.user;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails<T> extends UserDetails, CredentialsContainer {

    T getId();
}