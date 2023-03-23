package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.subscription.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findBySubscriberIdAndUserId(Long subscriberId, Long uid);

    List<Subscription> findAllBySubscriberIdAndSubscribedIsFalse(Long subscriberId);

    List<Subscription> findAllBySubscriberIdAndSubscribed(Long subscriberId, boolean isActive, Pageable pageable);

    Long countByUserId(Long uid);
}
