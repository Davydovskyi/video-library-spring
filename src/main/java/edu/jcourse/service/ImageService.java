package edu.jcourse.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${app.image.bucket}")
    private final String bucket;

    @SneakyThrows
    public void upload(String imagePath, InputStream content) {
        Path path = Path.of(bucket, imagePath);

        try (content) {
            Files.createDirectories(path.getParent());
            Files.write(path, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath) {
        Path path = Path.of(bucket, imagePath);
        return Files.exists(path)
                ? Optional.of(Files.readAllBytes(path))
                : Optional.empty();
    }
}