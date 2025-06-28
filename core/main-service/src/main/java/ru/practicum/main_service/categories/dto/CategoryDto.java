package ru.practicum.main_service.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Integer id;
    @NotBlank(message = "Category name should not be blank")
    @Size(min = 1, max = 50, message = "Category name should be between 1 and 50 characters")
    private String name;
}
