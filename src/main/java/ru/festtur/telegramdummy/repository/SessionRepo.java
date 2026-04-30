package ru.festtur.telegramdummy.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.festtur.telegramdummy.reference.dto.BookingSession;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionRepo implements ISessionRepo {

    private static final String SESSION_KEY = "session:";
    private static final Duration TTL = Duration.ofHours(24);

    private final ObjectMapper mapper;
    private final RedisTemplate<String, String> template;


    @Override
    public Optional<BookingSession> find(final Long userId) {
        var key = SESSION_KEY + userId;
        var json = template.opsForValue().get(key);
        if (json == null) return Optional.empty();
        log.info("for user {} session not founded", userId);
        try {
            return Optional.of(mapper.readValue(json, BookingSession.class));
        } catch (JacksonException e) {
            throw new RuntimeException("failed to restore session %d".formatted(userId), e);
        }
    }

    @Override
    public void save(final Long userId, final BookingSession session) {
        try {
            var key = SESSION_KEY + userId;
            var json = mapper.writeValueAsString(session);
            template.opsForValue().set(key, json, TTL);
        } catch (JacksonException e) {
            throw new RuntimeException("failed to save session %d".formatted(userId), e);
        }
    }

    @Override
    public void delete(final Long userId) {
        var key = SESSION_KEY + userId;
        template.delete(key);
    }

}
