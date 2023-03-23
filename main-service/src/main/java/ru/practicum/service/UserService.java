package ru.practicum.service;

import ru.practicum.dto.user.UserCreationDto;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto saveUser(UserCreationDto user);

    void deleteUser(Long uid);
}
