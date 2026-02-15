package ru.festtur.telegramdummy.repository;

import ru.festtur.telegramdummy.reference.dto.BookingSession;

import java.util.Optional;

public interface ISessionRepo {
    Optional<BookingSession> find(Long userId);
    void save(Long userId, BookingSession session);
    void delete(Long userId);
}
