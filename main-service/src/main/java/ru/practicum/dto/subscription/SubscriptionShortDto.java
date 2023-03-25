package ru.practicum.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserShortDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionShortDto {

    private Long id;
    private UserShortDto user;
}
