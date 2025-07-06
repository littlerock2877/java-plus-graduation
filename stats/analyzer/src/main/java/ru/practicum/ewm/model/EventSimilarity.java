package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_similarity")
public class EventSimilarity {
    @EmbeddedId
    private EventSimilarityId eventSimilarityId;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Instant actionDate;
}