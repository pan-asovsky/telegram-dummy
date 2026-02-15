package ru.festtur.telegramdummy.reference.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.festtur.telegramdummy.reference.dto.question.AnswerEntry;
import ru.festtur.telegramdummy.reference.dto.tour.TourDates;
import ru.festtur.telegramdummy.reference.enums.AccommodationType;
import ru.festtur.telegramdummy.reference.enums.BusSeatsPreferences;
import ru.festtur.telegramdummy.reference.enums.TourType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class BookingSession {

    private final Long userId;
    private Step step;

    private TourType type;
    private String destination;
    private String code;
    private TourDates dates;
    private Integer participants;

    private final List<AnswerEntry> answers;
    private final Integer totalQuestions;
    private Integer currentQuestionIndex;

    private BusSeatsPreferences seats;
    private AccommodationType accommodation;

    private BookingSession(Long userId, Integer total, Step initialStep) {
        this.userId = Objects.requireNonNull(userId);
        this.totalQuestions = Objects.requireNonNull(total);
        this.step = initialStep;
        this.currentQuestionIndex = 0;
        this.answers = new ArrayList<>();
    }

    @JsonCreator
    BookingSession(
        @JsonProperty("userId") Long userId,
        @JsonProperty("step") Step step,
        @JsonProperty("type") TourType type,
        @JsonProperty("destination") String destination,
        @JsonProperty("code") String code,
        @JsonProperty("dates") TourDates dates,
        @JsonProperty("participants") Integer participants,
        @JsonProperty("answers") List<AnswerEntry> answers,
        @JsonProperty("totalQuestions") Integer totalQuestions,
        @JsonProperty("questionIndex") Integer currentQuestionIndex,
        @JsonProperty("seats") BusSeatsPreferences seats,
        @JsonProperty("accommodation") AccommodationType accommodation
    ) {
        this.userId = Objects.requireNonNull(userId);
        this.step = step != null ? step : Step.SELECT_TYPE;

        this.type = type;
        this.destination = destination;
        this.code = code;
        this.dates = dates;
        this.participants = participants;

        this.totalQuestions = Objects.requireNonNull(totalQuestions);

        this.answers = new ArrayList<>();
        if (answers != null) {
            this.answers.addAll(answers);
        }

        this.currentQuestionIndex =
            currentQuestionIndex != null ? currentQuestionIndex : this.answers.size();
        if (this.currentQuestionIndex > this.totalQuestions) {
            throw new IllegalStateException("corrupted session: too many answers");
        }

        this.seats = seats;
        this.accommodation = accommodation;
    }


    public static BookingSession start(Long userId, Integer totalQuestions) {
        return new BookingSession(userId, totalQuestions, Step.SELECT_TYPE);
    }

    public static BookingSession startWithTourCode(Long userId, Integer totalQuestions, String code) {
        var session = new BookingSession(userId, totalQuestions, Step.SELECT_DATES);
        session.code = Objects.requireNonNull(code);
        return session;
    }

    public void selectType(TourType type) {
        requireStep(Step.SELECT_TYPE);
        this.type = type;
        this.step = Step.SELECT_DESTINATION;
    }

    public void selectDestination(String dest) {
        requireStep(Step.SELECT_DESTINATION);
        this.destination = dest;
        this.step = Step.SELECT_DATES;
    }

    public void selectDates(TourDates dates) {
        requireStep(Step.SELECT_DATES);
        this.dates = dates;
        this.step = Step.SELECT_PARTICIPANTS;
    }

    public void selectParticipants(Integer part) {
        requireStep(Step.SELECT_PARTICIPANTS);
        this.participants = part;

        if (this.totalQuestions == 0) this.step = Step.SELECT_BUS_SEATS;
        else this.step = Step.ANSWER_QUESTIONS;
    }

    public void answerQuestion(String code, String answer) {
        requireStep(Step.ANSWER_QUESTIONS);

        if (currentQuestionIndex >= totalQuestions) {
            throw new IllegalStateException("too many answers, current: %d, total: %d"
                .formatted(currentQuestionIndex, totalQuestions)
            );
        }

        answers.add(new AnswerEntry(code, answer));
        currentQuestionIndex++;

        if (currentQuestionIndex.equals(totalQuestions)) this.step = Step.SELECT_BUS_SEATS;
    }

    public void selectSeats(BusSeatsPreferences seats) {
        requireStep(Step.SELECT_BUS_SEATS);
        this.seats = seats;
        this.step = Step.SELECT_ACCOMMODATION;
    }

    public void selectAccommodation(AccommodationType acc) {
        requireStep(Step.SELECT_ACCOMMODATION);
        this.accommodation = acc;
    }

    public void review() {
        requireStep(Step.SELECT_ACCOMMODATION);
        this.step = Step.CONFIRM;
    }

    public void confirm() {
        requireStep(Step.CONFIRM);
        this.step = Step.COMPLETED;
    }


    private void requireStep(Step expected) {
        if (this.step != expected) {
            throw new IllegalStateException(
                "invalid step transition: expected: %s, actual: %s".formatted(expected, step)
            );
        }
    }

    enum Step {
        SELECT_TYPE,
        SELECT_DESTINATION,
        SELECT_DATES,
        SELECT_PARTICIPANTS,
        ANSWER_QUESTIONS,
        SELECT_BUS_SEATS,
        SELECT_ACCOMMODATION,
        CONFIRM,
        COMPLETED
    }
}
