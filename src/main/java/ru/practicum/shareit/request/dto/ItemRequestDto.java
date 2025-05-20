package ru.practicum.shareit.request.dto;

import lombok.*;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requestor;
}
