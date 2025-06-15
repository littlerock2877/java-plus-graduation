package ru.practicum.main_service.compilation.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_service.event.model.Event;

import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "compilations")
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") }
    )
    Collection<Event> events;
    @Column(name = "pinned")
    boolean pinned;
    @Column(name = "title")
    String title;
}
