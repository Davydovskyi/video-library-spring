package edu.jcourse.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.validation.EmailInfo;
import edu.jcourse.validation.MultipartFileInfo;
import edu.jcourse.validation.group.CreateAction;
import edu.jcourse.validation.group.UpdateAction;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static edu.jcourse.util.RegexUtil.EMAIL_REGEX;

@Builder
@FieldNameConstants
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserCreateEditDto(
        @NotEmpty(message = "{registration.error.email.empty}")
        @Email(message = "{registration.error.email.format}", regexp = EMAIL_REGEX)
        @EmailInfo(groups = {CreateAction.class})
        String email,

        @NotBlank(groups = {CreateAction.class}, message = "{registration.error.password.empty}")
        @Size(min = 8, message = "{registration.error.password.invalid}")
        String rawPassword,

        @NotBlank(message = "{registration.error.username.empty}")
        String userName,

        @NotNull(message = "{registration.error.birthdate.empty}")
        @Past(message = "{registration.error.birthdate.invalid}")
        LocalDate birthDate,

        @NotNull(message = "{registration.error.role.empty}")
        Role role,

        @NotNull(message = "{registration.error.gender.empty}")
        Gender gender,

        @MultipartFileInfo(groups = {CreateAction.class, UpdateAction.class})
        MultipartFile image) {
}