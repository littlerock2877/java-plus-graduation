package ru.practicum.request.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be blank")
    @Length(min = 6, max = 254, message = "Email length validation")
    private String email;

    @NotBlank(message = "User name should not be blank")
    @Length(min = 2, max = 250, message = "Name length validation")
    private String name;
}