package ru.practicum.explore_with_me.main.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.main.dao.converter.CategoryMapper;
import ru.practicum.explore_with_me.main.dao.entity.CategoryEntity;
import ru.practicum.explore_with_me.main.dao.repository.CategoryRepository;
import ru.practicum.explore_with_me.main.dao.repository.EventRepository;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.category.CategoryDto;
import ru.practicum.explore_with_me.main.exception.ConflictException;
import ru.practicum.explore_with_me.main.exception.NotFoundException;
import ru.practicum.explore_with_me.main.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryAdminDto createCategory(CategoryAdminCreateRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new ConflictException("Category already exists");
        }

        CategoryEntity categoryEntity = categoryMapper.toCategoryEntity(dto);
        categoryRepository.save(categoryEntity);
        log.debug("Category was created with id = {}", categoryEntity.getId());
        return categoryMapper.toCategoryAdminDto(categoryEntity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category does not exist with id = " + id);
        }
        if (eventRepository.existsByCategory_Id(id)) {
            throw new ConflictException("Category has events");
        }

        categoryRepository.deleteById(id);
        log.debug("Category with id = {} was deleted", id);
    }

    @Override
    @Transactional
    public CategoryAdminDto updateCategory(Long id, CategoryAdminUpdateRequestDto dto) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " does not exist"));
        if (categoryRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new ConflictException("Category with name " + dto.getName() + " already exists");
        }

        categoryEntity.setName(dto.getName());
        log.debug("Category with id = {} was updated", id);
        return categoryMapper.toCategoryAdminDto(categoryEntity);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.debug("Get category list with from = {} and size = {}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        log.debug("Get category with id = {}", id);
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id = " + id));
        return categoryMapper.toCategoryDto(category);
    }
}
