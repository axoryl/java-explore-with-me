package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationCreationDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAllCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long id);

    CompilationDto saveCompilation(CompilationCreationDto compilation);

    CompilationDto updateCompilation(CompilationUpdateDto compilation, Long compId);

    void deleteCompilation(Long compId);
}
