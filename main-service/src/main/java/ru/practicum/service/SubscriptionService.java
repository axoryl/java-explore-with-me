package ru.practicum.service;

import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.dto.subscription.SubscriptionShortDto;

import java.util.List;

public interface SubscriptionService {

    List<SubscriptionShortDto> getAllSubscriptions(Long subscriberId, boolean isActive, int from, int size);

    List<EventShortDto> getUserEvents(Long subscriberId, Long uid, int from, int size);

    List<EventShortDto> getEventsWhereUserParticipates(Long subscriberId, Long uid, int from, int size);

    Long getNumberOfSubscribers(Long uid);

    SubscriptionDto subscribe(Long subscriberId, Long uid);

    SubscriptionDto unsubscribe(Long subscriberId, Long uid);

    void deleteInactiveSubscriptions(Long subscriberId);
}
