package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.UserRepository;
import edu.jcourse.dto.user.AdaptedUserDetails;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserFilter;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.mapper.user.UserCreateEditMapper;
import edu.jcourse.mapper.user.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static edu.jcourse.database.entity.QUser.user;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.email(), user.email::containsIgnoreCase)
                .add(filter.username(), user.userName::containsIgnoreCase)
                .buildAnd();

        UserFilter.Sort sortBy = Optional.ofNullable(filter.sortBy())
                .orElse(UserFilter.Sort.USERNAME);

        Sort sort = Sort.by(sortBy.getName()).ascending();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return userRepository.findAll(predicate, pageRequest)
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userReadMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(user -> userCreateEditMapper.map(userDto, user))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new AdaptedUserDetails(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}