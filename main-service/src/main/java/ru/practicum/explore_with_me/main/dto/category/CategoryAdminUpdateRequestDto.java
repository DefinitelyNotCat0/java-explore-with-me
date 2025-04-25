package ru.practicum.explore_with_me.main.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryAdminUpdateRequestDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
