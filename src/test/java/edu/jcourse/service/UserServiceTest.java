package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.database.entity.User;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.UserRepository;
import edu.jcourse.dto.user.AdaptedUserDetails;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserFilter;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.mapper.user.UserCreateEditMapper;
import edu.jcourse.mapper.user.UserReadMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.jcourse.database.entity.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private UserCreateEditMapper userCreateEditMapper;
    @Mock
    private ImageService imageService;
    @Mock
    private Pageable pageable;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private InputStream inputStream;
    @InjectMocks
    private UserService userService;

    @SneakyThrows
    @Test
    void create() {
        doReturn(false).when(multipartFile).isEmpty();
        doReturn("file").when(multipartFile).getOriginalFilename();
        doReturn(inputStream).when(multipartFile).getInputStream();
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(multipartFile);
        User user = buildUser(1L);
        doReturn(user).when(userCreateEditMapper).map(any());
        doReturn(user).when(userRepository).save(any());
        UserReadDto expectedResult = buildUserReadDto(1L);
        doReturn(expectedResult).when(userReadMapper).map(any());

        UserReadDto actualResult = userService.create(userCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(multipartFile).isEmpty();
        verify(multipartFile).getOriginalFilename();
        verify(multipartFile).getInputStream();
        verify(imageService).upload("file", inputStream);
        verify(userCreateEditMapper).map(userCreateEditDto);
        verify(userRepository).save(user);
        verify(userReadMapper).map(user);
        verifyNoMoreInteractions(userRepository, imageService, userCreateEditMapper, userReadMapper, multipartFile, inputStream);
    }

    @Test
    void createWhenImageIsEmpty() {
        doReturn(true).when(multipartFile).isEmpty();
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(multipartFile);
        User user = buildUser(1L);
        doReturn(user).when(userCreateEditMapper).map(any());
        doReturn(user).when(userRepository).save(any());
        UserReadDto expectedResult = buildUserReadDto(1L);
        doReturn(expectedResult).when(userReadMapper).map(any());

        UserReadDto actualResult = userService.create(userCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(multipartFile).isEmpty();
        verify(userCreateEditMapper).map(userCreateEditDto);
        verify(userRepository).save(user);
        verify(userReadMapper).map(user);
        verifyNoInteractions(imageService, inputStream);
        verifyNoMoreInteractions(userRepository, imageService, userCreateEditMapper, userReadMapper, multipartFile);
    }

    @Test
    void createWhenUserIsNotSaved() {
        doReturn(true).when(multipartFile).isEmpty();
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(multipartFile);
        User user = buildUser(1L);
        doReturn(user).when(userCreateEditMapper).map(any());
        doReturn(null).when(userRepository).save(any());

        assertThrowsExactly(NoSuchElementException.class, () -> userService.create(userCreateEditDto));

        verify(multipartFile).isEmpty();
        verify(userCreateEditMapper).map(userCreateEditDto);
        verify(userRepository).save(user);
        verifyNoInteractions(imageService, inputStream, userReadMapper);
        verifyNoMoreInteractions(userRepository, imageService, userCreateEditMapper, userReadMapper, multipartFile);
    }

    @Test
    void findAll() {
        UserFilter filter = buildUserFilter();
        Predicate predicate = buildPredicate();
        doReturn(0).when(pageable).getPageNumber();
        doReturn(10).when(pageable).getPageSize();
        PageRequest pageRequest = buildPageRequest();
        List<User> users = List.of(buildUser(1L), buildUser(2L));
        doReturn(new PageImpl<>(users)).when(userRepository).findAll(any(Predicate.class), any(PageRequest.class));
        doReturn(buildUserReadDto(1L)).when(userReadMapper).map(users.get(0));
        doReturn(buildUserReadDto(2L)).when(userReadMapper).map(users.get(1));
        PageImpl<UserReadDto> expectedResult = new PageImpl<>(List.of(buildUserReadDto(1L), buildUserReadDto(2L)));

        Page<UserReadDto> actualResult = userService.findAll(filter, pageable);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(pageable).getPageNumber();
        verify(pageable).getPageSize();
        verify(userRepository).findAll(predicate, pageRequest);
        verify(userReadMapper).map(users.get(0));
        verify(userReadMapper).map(users.get(1));
        verifyNoMoreInteractions(userRepository, userReadMapper, pageable);
    }

    @Test
    void findAllWhenUserNotFound() {
        UserFilter filter = buildUserFilter();
        Predicate predicate = buildPredicate();
        doReturn(0).when(pageable).getPageNumber();
        doReturn(10).when(pageable).getPageSize();
        PageRequest pageRequest = buildPageRequest();
        doReturn(Page.empty()).when(userRepository).findAll(any(Predicate.class), any(PageRequest.class));

        Page<UserReadDto> actualResult = userService.findAll(filter, pageable);

        assertThat(actualResult).isEmpty();
        verify(pageable).getPageNumber();
        verify(pageable).getPageSize();
        verify(userRepository).findAll(predicate, pageRequest);
        verifyNoMoreInteractions(userRepository, userReadMapper, pageable);
    }

    @Test
    void findById() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        UserReadDto expectedResult = buildUserReadDto(1L);
        doReturn(expectedResult).when(userReadMapper).map(any());

        Optional<UserReadDto> actualResult = userService.findById(1L);

        assertThat(actualResult).contains(expectedResult);
        verify(userRepository).findById(1L);
        verify(userReadMapper).map(user);
        verifyNoMoreInteractions(userRepository, userReadMapper);
    }

    @Test
    void findByIdWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(any());

        Optional<UserReadDto> actualResult = userService.findById(1L);

        assertThat(actualResult).isEmpty();
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, userReadMapper);
    }

    @Test
    void findByEmail() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(any());
        UserReadDto expectedResult = buildUserReadDto(1L);
        doReturn(expectedResult).when(userReadMapper).map(any());

        Optional<UserReadDto> actualResult = userService.findByEmail("email");

        assertThat(actualResult).contains(expectedResult);
        verify(userRepository).findByEmail("email");
        verify(userReadMapper).map(user);
        verifyNoMoreInteractions(userRepository, userReadMapper);
    }

    @Test
    void findByEmailWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findByEmail(any());

        Optional<UserReadDto> actualResult = userService.findByEmail("email");

        assertThat(actualResult).isEmpty();
        verify(userRepository).findByEmail("email");
        verifyNoMoreInteractions(userRepository, userReadMapper);
    }

    @Test
    void update() {
        doReturn(true).when(multipartFile).isEmpty();
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(multipartFile);
        User user = buildUser(1L);
        doReturn(user).when(userCreateEditMapper).map(any(), any());
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(user).when(userRepository).saveAndFlush(any());
        UserReadDto expectedResult = buildUserReadDto(1L);
        doReturn(expectedResult).when(userReadMapper).map(any());

        Optional<UserReadDto> actualResult = userService.update(1L, userCreateEditDto);

        assertThat(actualResult).contains(expectedResult);
        verify(multipartFile).isEmpty();
        verify(userCreateEditMapper).map(userCreateEditDto, user);
        verify(userRepository).findById(1L);
        verify(userRepository).saveAndFlush(user);
        verify(userReadMapper).map(user);
        verifyNoInteractions(imageService);
        verifyNoMoreInteractions(userRepository, userReadMapper, userCreateEditMapper);
    }

    @Test
    void updateWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(any());
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(null);

        Optional<UserReadDto> actualResult = userService.update(1L, userCreateEditDto);

        assertThat(actualResult).isEmpty();
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(imageService, userReadMapper, userCreateEditMapper);
    }

    @Test
    void delete() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findById(any());

        boolean actualResult = userService.delete(1L);

        assertThat(actualResult).isTrue();
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
        verify(userRepository).flush();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(any());

        boolean actualResult = userService.delete(1L);

        assertThat(actualResult).isFalse();
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsername() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());
        AdaptedUserDetails expectedResult = new AdaptedUserDetails(1L, "email", "password", Collections.singleton(Role.USER));

        AdaptedUserDetails actualResult = (AdaptedUserDetails) userService.loadUserByUsername("email");

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(userRepository).findByUsername("email");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsernameWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(any());

        assertThrowsExactly(UsernameNotFoundException.class, () -> userService.loadUserByUsername("email"));
        verify(userRepository).findByUsername("email");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findImage() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        byte[] expectedResult = new byte[4];
        doReturn(Optional.of(expectedResult)).when(imageService).get(any());

        Optional<byte[]> actualResult = userService.findImage(1L);

        assertThat(actualResult).contains(expectedResult);
        verify(userRepository).findById(1L);
        verify(imageService).get("image");
        verifyNoMoreInteractions(userRepository, imageService);
    }

    @Test
    void findImageWhenUserNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(any());

        Optional<byte[]> actualResult = userService.findImage(1L);

        assertThat(actualResult).isEmpty();
        verify(userRepository).findById(1L);
        verifyNoInteractions(imageService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findImageWhenImageNotFound() {
        User user = buildUser(1L);
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(Optional.empty()).when(imageService).get(any());
        Optional<byte[]> actualResult = userService.findImage(1L);

        assertThat(actualResult).isEmpty();
        verify(userRepository).findById(1L);
        verify(imageService).get("image");
        verifyNoMoreInteractions(userRepository, imageService);
    }

    private UserCreateEditDto buildUserCreateEditDto(MultipartFile image) {
        return UserCreateEditDto.builder()
                .email("email")
                .rawPassword("password")
                .userName("username")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now())
                .image(image)
                .role(Role.USER)
                .build();
    }

    private UserReadDto buildUserReadDto(Long id) {
        return UserReadDto.builder()
                .id(id)
                .email("email")
                .userName("username")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .image("image")
                .build();
    }

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .password("password")
                .userName("username")
                .userImage("image")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .email("email")
                .build();
    }

    private UserFilter buildUserFilter() {
        return UserFilter.builder()
                .email("email")
                .userName("username")
                .build();
    }

    private Predicate buildPredicate() {
        return QPredicates.builder()
                .add("email", user.email::containsIgnoreCase)
                .add("username", user.userName::containsIgnoreCase)
                .buildAnd();
    }

    private PageRequest buildPageRequest() {
        return PageRequest.of(0, 10, Sort.by(User.Fields.userName).ascending());
    }
}