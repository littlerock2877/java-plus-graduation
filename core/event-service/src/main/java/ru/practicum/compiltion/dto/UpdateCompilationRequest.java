package ru.practicum.compiltion.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    @Nullable
    private List<Integer> events;

    private Boolean pinned = false;

    @Size(min = 1, max = 50)
    private String title;
}