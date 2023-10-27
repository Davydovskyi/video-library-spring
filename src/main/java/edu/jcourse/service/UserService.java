package edu.jcourse.service;

import edu.jcourse.database.repository.UserRepository;
import edu.jcourse.dto.UserCreateEditDto;
import edu.jcourse.dto.UserReadDto;
import edu.jcourse.mapper.UserCreateEditMapper;
import edu.jcourse.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    @Transactional
    public UserReadDto create(UserCreateEditDto user) {
        return Optional.of(user)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public Optional<UserReadDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userReadMapper::map);
    }
}