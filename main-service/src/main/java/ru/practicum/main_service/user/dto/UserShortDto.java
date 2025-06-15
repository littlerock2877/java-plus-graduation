package ru.practicum.main_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {
    private Integer id;

    @NotBlank(message = "User name should not be blank")
    private String name;
}