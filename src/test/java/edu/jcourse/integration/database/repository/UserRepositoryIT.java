package edu.jcourse.integration.database.repository;

import edu.jcourse.database.entity.User;
import edu.jcourse.database.repository.UserRepository;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private static final String USERNAME = "user1@gmail.com";
    private final UserRepository userRepository;

    @Test
    void findByUsername() {
        Optional<User> user = userRepository.findByUsername(USERNAME);

        assertThat(user).isPresent();
    }

    @Test
    void findByUsernameWhenNotFound() {
        Optional<User> user = userRepository.findByUsername("dummy@gmail.com");

        assertThat(user).isEmpty();
    }
}