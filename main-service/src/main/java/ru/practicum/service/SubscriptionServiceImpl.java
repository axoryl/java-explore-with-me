package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.dto.subscription.SubscriptionShortDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.SubscriptionMapper;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.subscription.Subscription;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.SubscriptionRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.StringTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.SubscriptionMapper.mapToSubscriptionDto;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<SubscriptionShortDto> getAllSubscriptions(final Long subscriberId, final boolean isActive,
                                                          final int from, final int size) {
        findUserOrThrowNotFoundException(subscriberId);
        final var pageable = PageRequest.of(from / size, size);
        final var subscriptions = subscriptionRepository.findAllBySubscriberIdAndSubscribed(subscriberId,
                isActive, pageable);

        return subscriptions.stream()
                .map(SubscriptionMapper::mapToSubscriptionShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getUserEvents(final Long subscriberId, final Long uid,
                                             final int from, final int size) {
        checkSubscription(subscriberId, uid);

        final var pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
        final var eventDate = LocalDateTime.now().plusHours(2);

        return eventRepository.findAllByParameters(uid, eventDate, pageable).stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getEventsWhereUserParticipates(final Long subscriberId, final Long uid,
                                                              final int from, final int size) {
        checkSubscription(subscriberId, uid);

        final var pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
        final var eventDate = LocalDateTime.now().plusHours(2);
        final var eventIds = requestRepository.findAllByRequesterAndStatus(uid, RequestStatus.CONFIRMED).stream()
                .map(Request::getEvent)
                .collect(Collectors.toList());

        return eventRepository.findAll(eventIds, eventDate, pageable).stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getNumberOfSubscribers(final Long uid) {
        findUserOrThrowNotFoundException(uid);

        return subscriptionRepository.countByUserId(uid);
    }

    @Override
    public SubscriptionDto subscribe(final Long subscriberId, final Long uid) {
        final var subscriber = findUserOrThrowNotFoundException(subscriberId);
        final var user = findUserOrThrowNotFoundException(uid);
        final var subscription = subscriptionRepository.findBySubscriberIdAndUserId(subscriberId, uid)
                .orElse(null);

        if (subscription == null) {
            return mapToSubscriptionDto(subscriptionRepository.save(Subscription.builder()
                    .subscriber(subscriber)
                    .user(user)
                    .subscribed(true)
                    .build()));
        }

        if (subscription.isSubscribed()) {
            throw new BadRequestException("The subscription has already been subscribed.");
        }

        subscription.setSubscribed(true);
        return mapToSubscriptionDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionDto unsubscribe(final Long subscriberId, final Long uid) {
        final var subscription = findSubscriptionOrThrowNotFoundException(subscriberId, uid);

        if (!subscription.isSubscribed()) {
            throw new BadRequestException("The subscription has already been unsubscribed.");
        }

        subscription.setSubscribed(false);

        return mapToSubscriptionDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void deleteInactiveSubscriptions(final Long subscriberId) {
        findUserOrThrowNotFoundException(subscriberId);

        final var subscriptionIds = subscriptionRepository.findAllBySubscriberIdAndSubscribedIsFalse(subscriberId)
                .stream()
                .map(Subscription::getId)
                .collect(Collectors.toList());

        subscriptionRepository.deleteAllByIdInBatch(subscriptionIds);
    }

    private User findUserOrThrowNotFoundException(final Long uid) {
        return userRepository.findById(uid).orElseThrow(
                () -> new NotFoundException(StringTemplate.USER_NOT_FOUND, uid)
        );
    }

    private Subscription findSubscriptionOrThrowNotFoundException(final Long subscriberId, final Long uid) {
        return subscriptionRepository.findBySubscriberIdAndUserId(subscriberId, uid).orElseThrow(
                () -> new NotFoundException("Subscription not found")
        );
    }

    private void checkSubscription(final Long subscriberId, final Long uid) {
        subscriptionRepository.findBySubscriberIdAndUserId(subscriberId, uid).orElseThrow(
                () -> new NotFoundException("You are not following this user.")
        );
    }
}
