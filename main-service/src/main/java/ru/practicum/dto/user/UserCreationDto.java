package ru.practicum.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserCreationDto {

    @NotBlank
    private String name;

    @Email
    private String email;
}
