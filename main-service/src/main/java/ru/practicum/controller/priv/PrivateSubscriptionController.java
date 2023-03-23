package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.dto.subscription.SubscriptionShortDto;
import ru.practicum.service.SubscriptionService;
import ru.practicum.util.StringTemplate;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class PrivateSubscriptionController {

    private final SubscriptionService subscriptionService;
    private final String logTemplate = StringTemplate.PRIVATE_SUBSCRIPTION_LOG;

    @GetMapping("/{subscriberId}")
    public List<SubscriptionShortDto> getAllSubscriptions(@PathVariable Long subscriberId,
                                                          @RequestParam(required = false, defaultValue = "true") boolean active,
                                                          @RequestParam(required = false, defaultValue = "0") int from,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate + "subscriber id: [%d] from: [%d] size: [%d]",
                "GET SUBSCRIPTIONS", subscriberId, from, size));
        return subscriptionService.getAllSubscriptions(subscriberId, active, from, size);
    }

    @GetMapping("/{subscriberId}/{uid}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long subscriberId,
                                             @PathVariable Long uid,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate + "subscriber id: [%d] user id: [%d] from: [%d] size: [%d]",
                "GET USER EVENTS", subscriberId, uid, from, size));
        return subscriptionService.getUserEvents(subscriberId, uid, from, size);
    }

    @GetMapping("/{subscriberId}/participates/{uid}/events")
    public List<EventShortDto> getEventsWhereUserParticipates(@PathVariable Long subscriberId,
                                                              @PathVariable Long uid,
                                                              @RequestParam(required = false, defaultValue = "0") int from,
                                                              @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(String.format(logTemplate + "subscriber id: [%d] user id: [%d] from: [%d] size: [%d]",
                "GET EVENTS WHERE USER PARTICIPATES", subscriberId, uid, from, size));
        return subscriptionService.getEventsWhereUserParticipates(subscriberId, uid, from, size);
    }

    @GetMapping("/{uid}/subscribers")
    public Long getNumberOfSubscribers(@PathVariable Long uid) {
        log.info(String.format(logTemplate + "user id: [%d]", "GET NUMBER OF SUBSCRIBERS", uid));
        return subscriptionService.getNumberOfSubscribers(uid);
    }

    @PostMapping("/{subscriberId}/{uid}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribe(@PathVariable Long subscriberId, @PathVariable Long uid) {
        log.info(String.format(logTemplate + "subscriber id: [%d] user id: [%d]", "SUBSCRIBE", subscriberId, uid));
        return subscriptionService.subscribe(subscriberId, uid);
    }

    @PatchMapping("/{subscriberId}/{uid}/unsubscribe")
    public SubscriptionDto unsubscribe(@PathVariable Long subscriberId, @PathVariable Long uid) {
        log.info(String.format(logTemplate + "subscriber id: [%d] user id: [%d]", "UNSUBSCRIBE", subscriberId, uid));
        return subscriptionService.unsubscribe(subscriberId, uid);
    }

    @DeleteMapping("/{subscriberId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInactiveSubscriptions(@PathVariable Long subscriberId) {
        log.info(String.format(logTemplate + "subscriber id: [%d]", "DELETE INACTIVE SUBSCRIBE", subscriberId));
        subscriptionService.deleteInactiveSubscriptions(subscriberId);
    }
}
