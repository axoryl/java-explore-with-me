package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationCreationDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationUpdateDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.StringTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CompilationMapper.mapToCompilation;
import static ru.practicum.mapper.CompilationMapper.mapToCompilationDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllCompilations(final boolean pinned, final int from, final int size) {
        final var pageable = PageRequest.of(from / size, size);
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(CompilationMapper::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(final Long compId) {
        return mapToCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(StringTemplate.COMPILATION_NOT_FOUND, compId))
        );
    }

    @Transactional
    @Override
    public CompilationDto saveCompilation(final CompilationCreationDto compilation) {
        if (compilation.getTitle().isBlank()) {
            throw new BadRequestException("Field: title. Error: must not be blank. Value: null");
        }

        final var events = eventRepository.findAllById(compilation.getEvents());
        return mapToCompilationDto(compilationRepository.save(mapToCompilation(compilation, Set.copyOf(events))));
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(final CompilationUpdateDto compilation, Long compId) {
        final var compilationToUpdate = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(StringTemplate.COMPILATION_NOT_FOUND, compId)
        );

        if (compilation.getPinned() != null) {
            compilationToUpdate.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null && !compilation.getTitle().isBlank()) {
            compilationToUpdate.setTitle(compilation.getTitle());
        }
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            compilationToUpdate.setEvents(new HashSet<>(eventRepository.findAllById(compilation.getEvents())));
        }
        return mapToCompilationDto(compilationRepository.save(compilationToUpdate));
    }

    @Transactional
    @Override
    public void deleteCompilation(final Long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(StringTemplate.COMPILATION_NOT_FOUND, compId)
        );
        compilationRepository.deleteById(compId);
    }
}
