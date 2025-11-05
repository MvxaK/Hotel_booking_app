package org.cook.booking_system.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class EntityIdUtils {

    private EntityIdUtils() {}

    public static <T> List<Long> extractIds(List<T> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(EntityIdUtils::extractId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T> Long extractId(T entity) {
        if (entity == null) return null;
        try {
            return (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}