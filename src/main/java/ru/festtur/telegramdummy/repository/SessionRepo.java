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

@Repository
@RequiredArgsConstructor
public class SessionRepo implements ISessionRepo {

    private final ObjectMapper mapper;
    private final DefaultRedisScript<Long> upsertSession;
    private final RedisTemplate<String, String> template;

    private static final String SESSION_KEY = "session:";

    @Override
    public void save(final Long userId, final BookingSession session) {
        final var key = SESSION_KEY + userId;
        final var args = new ArrayList<String>();

        addArg(args, Field.TOUR_CODE, session.getTourCode());
        addArg(args, Field.DATES, session.getDates());
        addArg(args, Field.PARTICIPANTS, session.getParticipants());
        addArg(args, Field.ANSWERS, session.getAnswers());
        addArg(args, Field.QUESTION_INDEX, session.getCurrentQuestionIndex());
        addArg(args, Field.SEATS, session.getSeats());
        addArg(args, Field.PLACEMENT, session.getPlacement());

        if (args.isEmpty()) return;

        template.execute(
            upsertSession,
            Collections.singletonList(key),
            args.toArray()
        );
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
        TOUR_CODE("tourCode"),
        DATES("dates"),
        PARTICIPANTS("participants"),
        ANSWERS("answers"),
        QUESTION_INDEX("questionIndex"),
        SEATS("seats"),
        PLACEMENT("placement");

        private final String value;
    }

}
