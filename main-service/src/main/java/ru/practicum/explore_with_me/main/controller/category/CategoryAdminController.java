package ru.practicum.explore_with_me.main.controller.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.service.CategoryService;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryAdminDto createCategory(@RequestBody @Valid CategoryAdminCreateRequestDto dto) {
        log.debug("Controller: createCategory");
        return categoryService.createCategory(dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.debug("Controller: deleteCategory");
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryAdminDto updateCategory(@PathVariable @Positive Long catId,
                                           @RequestBody @Valid CategoryAdminUpdateRequestDto dto) {
        log.debug("Controller: updateCategory");
        return categoryService.updateCategory(catId, dto);
    }
}
