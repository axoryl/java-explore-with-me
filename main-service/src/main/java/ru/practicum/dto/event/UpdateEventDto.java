package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.event.UpdateEventState;

import java.time.LocalDateTime;

@Data
public class UpdateEventDto {

    private String annotation;

    private Long category;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private UpdateEventState stateAction;

    private String title;
}
