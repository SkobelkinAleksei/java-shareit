package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Item {
    private Long id;
    @NotNull
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;
}
