package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserCreationDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.StringTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.UserMapper.mapToUser;
import static ru.practicum.mapper.UserMapper.mapToUserDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        final var pageable = PageRequest.of(from / size, size);

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids, pageable).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserCreationDto user) {
        return mapToUserDto(userRepository.save(mapToUser(user)));
    }

    @Transactional
    @Override
    public void deleteUser(Long uid) {
        userRepository.findById(uid).orElseThrow(
                () -> new NotFoundException(StringTemplate.USER_NOT_FOUND, uid)
        );
        userRepository.deleteById(uid);
    }
}
