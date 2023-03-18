package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationCreationDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static Compilation mapToCompilation(CompilationCreationDto compilation, Set<Event> events) {
        return Compilation.builder()
                .events(events)
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::mapToEventShortDto)
                        .collect(Collectors.toList()))
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
    }
}
