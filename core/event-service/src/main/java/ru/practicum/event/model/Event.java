    package ru.practicum.event.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import ru.practicum.categories.model.Category;
    import ru.practicum.event.enums.EventState;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @Entity
    @Table(name = "events")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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

        @Column(name = "initiator_id")
        private Integer initiatorId;

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