package ru.practicum.mapper;

import ru.practicum.dto.user.UserCreationDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.user.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User mapToUser(UserCreationDto user) {
        return User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
