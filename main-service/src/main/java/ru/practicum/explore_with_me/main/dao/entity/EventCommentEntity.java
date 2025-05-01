package ru.practicum.explore_with_me.main.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "event_comments")
public class EventCommentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDay;
}
