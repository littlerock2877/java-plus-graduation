package ru.practicum.stat_server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import static org.springframework.http.HttpHeaders.DATE;

@Entity
@Table(name = "hits")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String ip;

    @DateTimeFormat(pattern = DATE)
    @Column(name = "ts", nullable = false)
    private LocalDateTime timestamp;
}