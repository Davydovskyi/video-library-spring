package edu.jcourse.integration.service;

import edu.jcourse.service.ImageService;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ImageServiceIT {

    private static final String IMAGE_PATH = "testImage.PNG";
    private final String bucket = "src/test/resources/image";
    private final ImageService imageService = new ImageService(bucket);

    @SneakyThrows
    @Test
    void upload() {
        String fileName = "test.png";
        @Cleanup InputStream inputStream = getClass().getClassLoader().getResourceAsStream("image/" + IMAGE_PATH);

        imageService.upload(fileName, Objects.requireNonNull(inputStream));

        assertThat(imageService.get(fileName)).isPresent();
        Files.delete(Path.of(bucket, fileName));
    }

    @Test
    void get() {
        assertThat(imageService.get(IMAGE_PATH)).isPresent();
    }

    @Test
    void getWhenNotExists() {
        assertThat(imageService.get("notExists.png")).isEmpty();
    }
}