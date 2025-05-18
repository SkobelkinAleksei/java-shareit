package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requestor;
}
