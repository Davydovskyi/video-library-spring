package edu.jcourse.integration;

import edu.jcourse.integration.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@Sql("classpath:sql/data.sql")
@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"USER", "ADMIN"})
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:16.2");

    @BeforeAll
    static void runContainer() {
        CONTAINER.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
    }
}