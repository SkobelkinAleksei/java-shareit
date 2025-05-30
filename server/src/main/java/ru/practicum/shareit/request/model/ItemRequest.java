package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@ToString
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "requestor_id", nullable = false)
    private Long requestorId;

    @Column(nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "requestId")
    private List<Item> items;
}
