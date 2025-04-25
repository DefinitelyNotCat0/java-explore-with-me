package ru.practicum.explore_with_me.main.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "event_requests")
public class EventRequestEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventRequestStatus status = EventRequestStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;
}
