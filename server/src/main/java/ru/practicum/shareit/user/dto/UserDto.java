package ru.practicum.shareit.user.dto;

import lombok.*;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
