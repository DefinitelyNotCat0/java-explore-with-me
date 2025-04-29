package ru.practicum.explore_with_me.main.controller.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminCreateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationAdminUpdateRequestDto;
import ru.practicum.explore_with_me.main.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.main.service.CompilationService;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createAdminCompilation(@RequestBody @Valid CompilationAdminCreateRequestDto dto) {
        log.debug("Controller: createAdminCompilation");
        return compilationService.createAdminCompilation(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdminCompilation(@PathVariable @Positive Long compId) {
        log.debug("Controller: deleteAdminCompilation");
        compilationService.deleteAdminCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateAdminCompilation(@PathVariable @Positive Long compId,
                                                 @RequestBody @Valid CompilationAdminUpdateRequestDto dto) {
        log.debug("Controller: updateAdminCompilation");
        return compilationService.updateAdminCompilation(compId, dto);
    }
}
