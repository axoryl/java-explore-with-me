package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationCreationDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationUpdateDto;
import ru.practicum.service.CompilationService;
import ru.practicum.util.StringTemplate;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCompilationController {

    private final CompilationService compilationService;
    private static final String logTemplate = StringTemplate.ADMIN_COMPILATION_LOG;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody CompilationCreationDto compilation) {
        log.info(String.format(logTemplate, "SAVE"));
        return compilationService.saveCompilation(compilation);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@RequestBody CompilationUpdateDto compilation, @PathVariable Long compId) {
        log.info(String.format(logTemplate, "UPDATE"));
        return compilationService.updateCompilation(compilation, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info(String.format(logTemplate, "DELETE"));
        compilationService.deleteCompilation(compId);
    }
}
