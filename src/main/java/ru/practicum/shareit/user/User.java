package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email(message = "Указан некорректный email")
    private String email;
}
