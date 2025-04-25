package ru.practicum.explore_with_me.main.dao.specfication;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dto.event.EventState;

import java.time.LocalDateTime;
import java.util.Set;

public class EventSpecification {

    private EventSpecification() {
    }

    public static Specification<EventEntity> withInitiatorIdIn(Set<Long> initiatorIdList) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(initiatorIdList)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(root.get("initiator").get("id").in(initiatorIdList));
        };
    }

    public static Specification<EventEntity> withStateIn(Set<EventState> stateList) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(stateList)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(root.get("state").in(stateList));
        };
    }

    public static Specification<EventEntity> withCategoryIdIn(Set<Long> categoryIdList) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(categoryIdList)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(root.get("category").get("id").in(categoryIdList));
        };
    }

    public static Specification<EventEntity> withEventDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return (root, query, criteriaBuilder) -> {
            if (dateFrom == null && dateTo == null) {
                return criteriaBuilder.conjunction();
            } else if (dateFrom == null) {
                return criteriaBuilder.lessThan(root.get("eventDate"), dateTo);
            } else if (dateTo == null) {
                return criteriaBuilder.greaterThan(root.get("eventDate"), dateFrom);
            }
            return criteriaBuilder.between(root.get("eventDate"), dateFrom, dateTo);
        };
    }

    public static Specification<EventEntity> withAnnotationOrDescriptionLike(String text) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(text) || text.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                            "%" + text.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + text.toLowerCase() + "%")
            );
        };
    }

    public static Specification<EventEntity> withPaidEquals(Boolean isPaid) {
        return (root, query, criteriaBuilder) -> {
            if (isPaid == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("paid"), isPaid);
        };
    }

    public static Specification<EventEntity> withOnlyAvailable(Boolean isOnlyAvailable) {
        return (root, query, criteriaBuilder) -> {
            if (isOnlyAvailable == null || !isOnlyAvailable) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("participantLimit"), 0),
                    criteriaBuilder.greaterThan(root.get("participantLimit"), root.join("confirmedRequests"))
            );
        };
    }
}
