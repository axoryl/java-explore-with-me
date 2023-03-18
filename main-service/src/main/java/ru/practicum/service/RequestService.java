package ru.practicum.service;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.UpdateRequestStatusDto;
import ru.practicum.dto.request.UpdateRequestsResultDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getAllUserRequests(Long uid);

    RequestDto saveRequest(Long uid, Long eventId);

    RequestDto cancelRequest(Long uid, Long requestId);

    UpdateRequestsResultDto updateRequestsStatus(Long uid, Long eventId,
                                                 UpdateRequestStatusDto updateRequestStatusDto);
}
