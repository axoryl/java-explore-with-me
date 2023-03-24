package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.UpdateRequestStatusDto;
import ru.practicum.dto.request.UpdateRequestsResultDto;
import ru.practicum.service.RequestService;
import ru.practicum.util.StringTemplate;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrivateRequestController {

    private final RequestService requestService;
    private static final String logTemplate = StringTemplate.PRIVATE_REQUEST_LOG;

    @GetMapping("/{uid}/requests")
    public List<RequestDto> getAllUserRequests(@PathVariable Long uid) {
        log.info(String.format(logTemplate, "GET ALL USER REQUESTS"));
        return requestService.getAllUserRequests(uid);
    }

    @PostMapping("/{uid}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequest(@PathVariable Long uid, @RequestParam Long eventId) {
        log.info(String.format(logTemplate, "SAVE"));
        return requestService.saveRequest(uid, eventId);
    }

    @PatchMapping("/{uid}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long uid, @PathVariable Long requestId) {
        log.info(String.format(logTemplate, "CANCEL"));
        return requestService.cancelRequest(uid, requestId);
    }

    @PatchMapping("/{uid}/events/{eventId}/requests")
    public UpdateRequestsResultDto updateRequestsStatus(@PathVariable Long uid,
                                                        @PathVariable Long eventId,
                                                        @RequestBody UpdateRequestStatusDto updateRequestStatusDto) {
        log.info(String.format(logTemplate, "UPDATE REQUESTS STATUS"));
        return requestService.updateRequestsStatus(uid, eventId, updateRequestStatusDto);
    }
}
