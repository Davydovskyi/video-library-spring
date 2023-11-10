package edu.jcourse.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AdaptedUserDetails extends User {

    private final Long id;

    public AdaptedUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}