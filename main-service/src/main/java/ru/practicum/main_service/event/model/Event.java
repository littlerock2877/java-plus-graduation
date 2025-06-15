package ru.practicum.main_service.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.user.model.User;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String annotation;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    private Long confirmedRequests;

    @Column(name = "created_On")
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    private Long views;
}