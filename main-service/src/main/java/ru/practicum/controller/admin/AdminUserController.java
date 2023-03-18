package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserCreationDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info(">>> ADMIN USER GET ALL --> user ids: [" + ids + "] from: [" + from + "] size: [" + size + "]");
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody UserCreationDto user) {
        log.info(">>> ADMIN USER SAVE --> user: [" + user + "]");
        return userService.saveUser(user);
    }

    @DeleteMapping("/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long uid) {
        log.info(">>> ADMIN USER DELETE --> user id: [" + uid + "]");
        userService.deleteUser(uid);
    }

}
