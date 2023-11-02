package edu.jcourse.mapper.user;

import edu.jcourse.database.entity.User;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserCreateEditDto from) {
        User user = new User();
        copy(from, user);
        return user;
    }

    @Override
    public User map(UserCreateEditDto from, User to) {
        copy(from, to);
        return to;
    }

    private void copy(UserCreateEditDto from, User to) {
        to.setEmail(from.email());
        to.setUserName(from.username());
        to.setBirthDate(from.birthDate());
        to.setRole(from.role());
        to.setGender(from.gender());

        Optional.ofNullable(from.rawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(to::setPassword);

        Optional.ofNullable(from.image())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(multipartFile -> to.setUserImage(multipartFile.getOriginalFilename()));
    }
}