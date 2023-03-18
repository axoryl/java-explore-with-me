package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.UpdateRequestStatusDto;
import ru.practicum.dto.request.UpdateRequestsResultDto;
import ru.practicum.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping("/{uid}/requests")
    public List<RequestDto> getAllUserRequests(@PathVariable Long uid) {
        log.info(">>> PRIVATE REQUEST GET ALL USER REQUESTS --> user id: [" + uid + "]");
        return requestService.getAllUserRequests(uid);
    }

    @PostMapping("/{uid}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequest(@PathVariable Long uid, @RequestParam Long eventId) {
        log.info(">>> PRIVATE REQUEST SAVE --> user id: [" + uid + "] event id: [" + eventId + "]");
        return requestService.saveRequest(uid, eventId);
    }

    @PatchMapping("/{uid}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long uid, @PathVariable Long requestId) {
        log.info(">>> PRIVATE REQUEST CANCEL --> user id: [" + uid + "] request id: [" + requestId + "]");
        return requestService.cancelRequest(uid, requestId);
    }

    @PatchMapping("/{uid}/events/{eventId}/requests")
    public UpdateRequestsResultDto updateRequestsStatus(@PathVariable Long uid,
                                                        @PathVariable Long eventId,
                                                        @RequestBody UpdateRequestStatusDto updateRequestStatusDto) {
        log.info(">>> PRIVATE REQUEST UPDATE REQUESTS STATUS --> user id: [" + uid + "] event id: [" + eventId + "]" +
                " requests: [" + updateRequestStatusDto + "]");
        return requestService.updateRequestsStatus(uid, eventId, updateRequestStatusDto);
    }
}
