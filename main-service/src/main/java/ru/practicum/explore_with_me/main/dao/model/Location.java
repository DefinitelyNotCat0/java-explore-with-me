package ru.practicum.explore_with_me.main.dao.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Location {

    private float lat;
    private float lon;
}
