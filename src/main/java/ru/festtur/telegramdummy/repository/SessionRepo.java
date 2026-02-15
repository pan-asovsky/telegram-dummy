package ru.festtur.telegramdummy.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import ru.festtur.telegramdummy.reference.dto.BookingSession;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SessionRepo implements ISessionRepo {

    private final ObjectMapper mapper;
    private final DefaultRedisScript<Long> upsertSession;
    private final RedisTemplate<String, String> template;

    private static final String SESSION_KEY = "session:";

    @Override
    public Optional<BookingSession> find(final Long userId) {
        var key = SESSION_KEY + userId;
        var entries = template.opsForHash().entries(key);
        if (entries == null || entries.isEmpty()) return Optional.empty();

        try {
            var json = entries.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().toString()
                ));
            var session = mapper.convertValue(json, BookingSession.class);
            return Optional.of(session);
        } catch (Exception e) {
            throw new IllegalStateException("failed to restore session for user: %d".formatted(userId), e);
        }
    }

    @Override
    public void save(final Long userId, final BookingSession session) {
        final var key = SESSION_KEY + userId;
        final var args = new ArrayList<String>();

        addArg(args, Field.USER_ID, session.getUserId());
        addArg(args, Field.STEP, session.getStep());
        addArg(args, Field.TOUR_TYPE, session.getType());
        addArg(args, Field.DESTINATION, session.getDestination());
        addArg(args, Field.TOUR_CODE, session.getCode());
        addArg(args, Field.DATES, session.getDates());
        addArg(args, Field.PARTICIPANTS, session.getParticipants());
        addArg(args, Field.ANSWERS, session.getAnswers());
        addArg(args, Field.TOTAL_QUESTIONS, session.getTotalQuestions());
        addArg(args, Field.QUESTION_INDEX, session.getCurrentQuestionIndex());
        addArg(args, Field.SEATS, session.getSeats());
        addArg(args, Field.ACCOMMODATION, session.getAccommodation());

        if (args.isEmpty()) return;

        template.execute(
            upsertSession,
            Collections.singletonList(key),
            args.toArray()
        );
    }

    @Override
    public void delete(final Long userId) {
        var key = SESSION_KEY + userId;
        template.delete(key);
    }

    private void addArg(final List<String> args, final Field f, final Object val) {
        if (val == null) return;

        try {
            final String serialized = (val instanceof String || val instanceof Number)
                ? String.valueOf(val)
                : mapper.writeValueAsString(val);

            args.add(f.value);
            args.add(serialized);
        } catch (JacksonException e) {
            throw new RuntimeException("Serialization failed for field: %s".formatted(f.value), e);
        }
    }

    @Getter
    @RequiredArgsConstructor
    enum Field {
        USER_ID("userId"),
        STEP("step"),
        TOUR_TYPE("type"),
        DESTINATION("destination"),
        TOUR_CODE("code"),
        DATES("dates"),
        PARTICIPANTS("participants"),
        ANSWERS("answers"),
        TOTAL_QUESTIONS("totalQuestions"),
        QUESTION_INDEX("questionIndex"),
        SEATS("seats"),
        ACCOMMODATION("accommodation");

        private final String value;
    }

}
