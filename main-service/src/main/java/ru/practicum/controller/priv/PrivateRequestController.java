package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/users")
public class PrivateRequestController {

    private final RequestService requestService;
    private final String logTemplate = StringTemplate.PRIVATE_REQUEST_LOG;

    @GetMapping("/{uid}/requests")
    public List<RequestDto> getAllUserRequests(@PathVariable Long uid) {
        log.info(String.format(logTemplate + "user id: [%d]", "GET ALL USER REQUESTS", uid));
        return requestService.getAllUserRequests(uid);
    }

    @PostMapping("/{uid}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequest(@PathVariable Long uid, @RequestParam Long eventId) {
        log.info(String.format(logTemplate + "user id: [%d] event id: [%d]", "SAVE", uid, eventId));
        return requestService.saveRequest(uid, eventId);
    }

    @PatchMapping("/{uid}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long uid, @PathVariable Long requestId) {
        log.info(String.format(logTemplate + "user id: [%d] request id: [%d]", "CANCEL", uid, requestId));
        return requestService.cancelRequest(uid, requestId);
    }

    @PatchMapping("/{uid}/events/{eventId}/requests")
    public UpdateRequestsResultDto updateRequestsStatus(@PathVariable Long uid,
                                                        @PathVariable Long eventId,
                                                        @RequestBody UpdateRequestStatusDto updateRequestStatusDto) {
        log.info(String.format(logTemplate + "user id: [%d] event id: [%d]",
                "UPDATE REQUESTS STATUS", uid, eventId));
        return requestService.updateRequestsStatus(uid, eventId, updateRequestStatusDto);
    }
}
