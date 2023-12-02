package edu.jcourse.mapper.user;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.database.entity.User;
import edu.jcourse.dto.user.UserCreateEditDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCreateEditMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserCreateEditMapper mapper;
    @Mock
    private MultipartFile multipartFile;

    @Test
    void mapForCreate() {
        doReturn(false).when(multipartFile).isEmpty();
        doReturn("image").when(multipartFile).getOriginalFilename();
        doReturn("encodedPassword").when(passwordEncoder).encode(any());
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("password", multipartFile);
        User expectedResult = buildUser("encodedPassword", "image");

        User actualResult = mapper.map(userCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(multipartFile).isEmpty();
        verify(multipartFile).getOriginalFilename();
        verify(passwordEncoder).encode("password");
        verifyNoMoreInteractions(multipartFile, passwordEncoder);
    }

    @Test
    void mapForEdit() {
        User user = buildUser("encodedPassword", "image");
        doReturn(false).when(multipartFile).isEmpty();
        doReturn("test").when(multipartFile).getOriginalFilename();
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto(null, multipartFile);
        User expectedResult = buildUser("encodedPassword", "test");

        User actualResult = mapper.map(userCreateEditDto, user);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(multipartFile).isEmpty();
        verify(multipartFile).getOriginalFilename();
        verifyNoMoreInteractions(multipartFile, passwordEncoder);
    }

    private UserCreateEditDto buildUserCreateEditDto(String password, MultipartFile image) {
        return UserCreateEditDto.builder()
                .email("email")
                .username("username")
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .rawPassword(password)
                .image(image)
                .build();
    }

    private User buildUser(String password, String image) {
        return User.builder()
                .email("email")
                .userName("username")
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .userImage(image)
                .password(password)
                .build();
    }
}