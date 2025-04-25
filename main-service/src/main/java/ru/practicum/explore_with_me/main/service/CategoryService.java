package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.category.CategoryAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryAdminDto createCategory(CategoryAdminCreateRequestDto dto);

    void deleteCategory(Long id);

    CategoryAdminDto updateCategory(Long id, CategoryAdminUpdateRequestDto dto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long id);
}
