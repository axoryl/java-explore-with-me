package ru.practicum.mapper;

import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.dto.subscription.SubscriptionShortDto;
import ru.practicum.model.subscription.Subscription;

import static ru.practicum.mapper.UserMapper.mapToUserShortDto;

public class SubscriptionMapper {

    private SubscriptionMapper() {
    }

    public static SubscriptionDto mapToSubscriptionDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .subscriberId(subscription.getSubscriber().getId())
                .isSubscribed(subscription.isSubscribed())
                .build();
    }

    public static SubscriptionShortDto mapToSubscriptionShortDto(Subscription subscription) {
        return SubscriptionShortDto.builder()
                .id(subscription.getId())
                .user(mapToUserShortDto(subscription.getUser()))
                .build();
    }
}
