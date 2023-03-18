package ru.practicum.mapper;

import ru.practicum.dto.event.EventCreationDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

import static ru.practicum.mapper.CategoryMapper.mapToCategoryDto;
import static ru.practicum.mapper.LocationMapper.mapToLocationDto;
import static ru.practicum.mapper.UserMapper.mapToUserShortDto;

public class EventMapper {

    private EventMapper() {
    }

    public static Event mapToEvent(EventCreationDto event,
                                   LocalDateTime createdOn,
                                   User user,
                                   Category category,
                                   Location location) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .category(category)
                .createdOn(createdOn)
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(user)
                .location(location)
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(EventState.PENDING)
                .title(event.getTitle())
                .build();
    }

    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapToCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(mapToUserShortDto(event.getInitiator()))
                .location(mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventDto mapToEventDto(Event event, long views, int confirmedRequests) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapToCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .confirmedRequests(confirmedRequests)
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(mapToUserShortDto(event.getInitiator()))
                .location(mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .category(mapToCategoryDto(event.getCategory()))
                .user(mapToUserShortDto(event.getInitiator()))
                .build();
    }
}
