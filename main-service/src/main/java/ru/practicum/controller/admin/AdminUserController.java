package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserCreationDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;
import ru.practicum.util.StringTemplate;

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
    private final String logTemplate = StringTemplate.ADMIN_USER_LOG;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info(String.format(logTemplate + "users: [%s] from: [%d] size: [%d]",
                "GET ALL USERS", ids, from, size));
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody UserCreationDto user) {
        log.info(String.format(logTemplate + "user: [%s]", "SAVE", user));
        return userService.saveUser(user);
    }

    @DeleteMapping("/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long uid) {
        log.info(String.format(logTemplate + "user id: [%d]", "DELETE", uid));
        userService.deleteUser(uid);
    }

}
