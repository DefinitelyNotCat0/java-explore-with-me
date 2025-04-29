package ru.practicum.explore_with_me.main.dao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.explore_with_me.main.dao.entity.CategoryEntity;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryEntity toCategoryEntity(CategoryAdminCreateRequestDto categoryAdminCreateRequestDto);

    CategoryAdminDto toCategoryAdminDto(CategoryEntity categoryEntity);

    CategoryDto toCategoryDto(CategoryEntity categoryEntity);
}
