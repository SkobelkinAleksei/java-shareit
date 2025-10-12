package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {
    @NotBlank(message = "Описание должно быть заполнено")
    @Size(max = 1000, message = "Описание не должно превышать 1к символов")
    String description;
}
