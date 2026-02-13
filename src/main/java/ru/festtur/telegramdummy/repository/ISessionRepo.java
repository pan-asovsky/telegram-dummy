package ru.festtur.telegramdummy.repository;

import ru.festtur.telegramdummy.reference.dto.BookingSession;

public interface ISessionRepo {
    void save(Long userId, BookingSession session);
}
