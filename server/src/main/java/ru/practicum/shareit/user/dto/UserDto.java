package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
