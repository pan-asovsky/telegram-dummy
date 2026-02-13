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
public class BookingSessionV2 {

    private final Long userId;
    private Step step;

    private TourType type;
    private String direction;
    private String tourCode;  //todo: подумать
    private TourDates dates;
    private Integer participants;

    private final List<AnswerEntry> answers;
    private final Integer totalQuestions;
    private Integer currentQuestionIndex;

    private BusSeatsPreferences seats;
    private AccommodationType accommodation;

    private BookingSessionV2(Long userId, Integer total) {
        Objects.requireNonNull(userId);
        this.userId = userId;
        this.step = Step.SELECT_TYPE;
        this.totalQuestions = total;
        this.answers = new ArrayList<>();
    }

    @JsonCreator
    BookingSessionV2(
        @JsonProperty("userId") Long userId,
        @JsonProperty("step") Step step,
        @JsonProperty("answers") List<AnswerEntry> answers,
        @JsonProperty("totalQuestions") Integer total
    ) {
        this.userId = userId;
        this.step = step;
        this.totalQuestions = total;
        this.answers = new ArrayList<>();
        if (answers != null) this.answers.addAll(answers);
    }

    public static BookingSessionV2 start(Long userId, Integer totalQuestions) {
        return new BookingSessionV2(userId, totalQuestions);
    }

    public void selectType(TourType type) {
        requireStep(Step.SELECT_TYPE);
        this.type = type;
        this.step = Step.SELECT_DESTINATION;
    }

    public void selectDestination(String dest) {
        requireStep(Step.SELECT_DESTINATION);
        this.direction = dest;
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
        this.step = Step.ANSWER_QUESTIONS;
    }

    public void answerQuestion(String code, String answer) {
        requireStep(Step.ANSWER_QUESTIONS);
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

    public void confirm() {
        requireStep(Step.SELECT_ACCOMMODATION);
        this.step = Step.CONFIRM;
    }

    public void complete() {
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
