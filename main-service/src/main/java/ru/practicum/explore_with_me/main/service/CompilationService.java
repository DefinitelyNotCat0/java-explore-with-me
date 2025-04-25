package ru.practicum.explore_with_me.main.service;

import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilationList(boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long id);

    CompilationDto createAdminCompilation(CompilationAdminCreateRequestDto dto);

    void deleteAdminCompilation(Long id);

    CompilationDto updateAdminCompilation(Long id, CompilationAdminUpdateRequestDto dto);
}
