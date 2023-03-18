package ru.practicum.mapper;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.request.Request;

public class RequestMapper {

    private RequestMapper() {
    }

    public static Request mapToRequest(RequestDto request) {
        return Request.builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();
    }

    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();
    }
}
