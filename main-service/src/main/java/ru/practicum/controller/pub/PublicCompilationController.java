package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.CompilationService;
import ru.practicum.util.StringTemplate;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;
    private final String logTemplate = StringTemplate.PUBLIC_COMPILATION_LOG;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false, defaultValue = "false") boolean pinned,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate + "pinned: [%b] from: [%d] size: [%d]",
                "GET ALL", pinned, from, size));
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info(String.format(logTemplate + "compilation id: [%d]", "GET BY ID", compId));
        return compilationService.getCompilationById(compId);
    }
}
