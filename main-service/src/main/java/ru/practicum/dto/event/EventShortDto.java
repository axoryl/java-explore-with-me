package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    private LocalDateTime eventDate;

    private UserShortDto user;

    private boolean paid;

    private String title;

    private long views;
}
