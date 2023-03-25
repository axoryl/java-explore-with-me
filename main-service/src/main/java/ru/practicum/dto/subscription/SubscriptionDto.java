package ru.practicum.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {

    private Long id;
    private Long userId;
    private Long subscriberId;
    private boolean isSubscribed;
}
