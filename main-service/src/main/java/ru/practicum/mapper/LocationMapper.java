package ru.practicum.mapper;

import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.location.Location;

public class LocationMapper {

    private LocationMapper() {
    }

    public static LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location mapToLocation(LocationDto location) {
        return Location.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
