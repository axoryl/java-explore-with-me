package ru.practicum.dto.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompilationCreationDto {

    private List<Long> events;

    private Boolean pinned;

    @NotBlank
    private String title;
}
