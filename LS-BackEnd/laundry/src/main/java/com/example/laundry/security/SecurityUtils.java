package com.example.laundry.security;

import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Utility helpers for resolving authenticated principals consistently across controllers and services.
 */
public final class SecurityUtils {
    private SecurityUtils() {
    }

    /**
     * Parses a UUID string safely without throwing on non-UUID principals (e.g. test mock usernames).
     */
    public static Optional<UUID> parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(value.trim()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    /**
     * Resolves the current entity by UUID principal first, falling back to phone/username lookup.
     */
    public static <T> T resolveCurrentUser(
            Authentication authentication,
            Function<UUID, Optional<T>> findById,
            Function<String, T> findByUsername
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String principal = authentication.getName();
        if (principal == null || principal.isBlank() || "anonymousUser".equals(principal)) {
            return null;
        }
        Optional<UUID> userId = parseUuid(principal);
        if (userId.isPresent()) {
            T byId = findById.apply(userId.get()).orElse(null);
            if (byId != null) {
                return byId;
            }
        }
        return findByUsername != null ? findByUsername.apply(principal) : null;
    }
}

