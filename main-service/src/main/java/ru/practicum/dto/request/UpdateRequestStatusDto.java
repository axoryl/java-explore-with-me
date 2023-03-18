package ru.practicum.dto.request;

import lombok.Data;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

@Data
public class UpdateRequestStatusDto {

    private List<Long> requestIds;
    private RequestStatus status;
}
