package ru.festtur.telegramdummy.reference.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import ru.festtur.telegramdummy.reference.dto.flow.FlowConfig;
import ru.festtur.telegramdummy.reference.dto.flow.FlowStep;
import ru.festtur.telegramdummy.reference.dto.question.AnswerEntry;
import ru.festtur.telegramdummy.reference.dto.tour.TourDates;
import ru.festtur.telegramdummy.reference.enums.AccommodationType;
import ru.festtur.telegramdummy.reference.enums.BusSeatsPreferences;
import ru.festtur.telegramdummy.reference.enums.FStep;
import ru.festtur.telegramdummy.reference.enums.TourType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
public class BookingSession {

    private final Long userId;
    private final FlowConfig flow;

    private final List<FStep> steps;
    private int currentStepIndex;

    private TourType type;
    private String destination;
    private String code;
    private TourDates dates;
    private Integer participants;

    private final List<AnswerEntry> answers;
    private final Integer totalQuestions;

    private BusSeatsPreferences seats;
    private AccommodationType accommodation;

    private BookingSession(final Long userId, final FlowConfig config) {
        this.userId = Objects.requireNonNull(userId, "userID cannot be null");
        this.flow = Objects.requireNonNull(config, "flow cannot be null");
        this.totalQuestions = config.getQuestions().size();
        this.steps = mapSteps(config.getSteps());
        this.currentStepIndex = 0;
        this.answers = new ArrayList<>();
    }

    private List<FStep> mapSteps(List<FlowStep> steps) {
        return steps.stream()
            .sorted(Comparator.comparingInt(FlowStep::getOrder))
            .map(fs -> FStep.valueOf(fs.getCode()))
            .toList();
    }

    public static BookingSession start(final Long userId, final FlowConfig config) {
        return new BookingSession(userId, config);
    }

    public static BookingSession startFromCode(final Long userId, final FlowConfig flow, final String tourCode) {
        if (tourCode == null || tourCode.isBlank()) {
            throw new IllegalArgumentException("tourCode cannot be null or blank");
        }

        final BookingSession session = new BookingSession(userId, flow);
        session.code = tourCode;
        return session;
    }

    public void nextStep(Object obj) {
        switch (steps.get(currentStepIndex)) {
            case SELECT_TYPE -> selectType((TourType) obj);
            case SELECT_DESTINATION -> selectDestination((String) obj);
            case SELECT_CODE -> selectCode((String) obj);
            case SELECT_DATES -> selectDates((TourDates) obj);
            case SELECT_PARTICIPANTS -> selectParticipants((Integer) obj);
            case ANSWER_QUESTIONS -> answerQuestion((AnswerEntry) obj);
            case SELECT_BUS_SEATS -> selectSeats((BusSeatsPreferences) obj);
            case SELECT_ACCOMMODATION -> selectAccommodation((AccommodationType) obj);
            case CONFIRM -> confirm();
        }
    }

    private void selectType(TourType type) {
        requireStep(FStep.SELECT_TYPE);
        this.type = validateType(type);
        advanceStep();
    }

    private TourType validateType(TourType type) {
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        return type;
    }

    private void selectDestination(String destination) {
        requireStep(FStep.SELECT_DESTINATION);
        this.destination = validateDestination(destination);
        advanceStep();
    }

    private String validateDestination(String destination) {
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination cannot be null or blank");
        return destination;
    }

    private void selectCode(String code) {
        requireStep(FStep.SELECT_CODE);
        this.code = validateCode(code);
        advanceStep();
    }

    private String validateCode(String code) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("code cannot be null or blank");
        return code;
    }

    private void selectDates(TourDates dates) {
        requireStep(FStep.SELECT_DATES);
        this.dates = validateDates(dates);
        advanceStep();
    }

    private TourDates validateDates(TourDates dates) {
        if (dates == null) throw new IllegalArgumentException("dates cannot be null");
        if (dates.getStart().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Tour dates cannot be in the past");
        if (dates.getEnd().isBefore(dates.getStart()))
            throw new IllegalArgumentException("End date must be after start date");
        return dates;
    }

    private void selectParticipants(Integer participants) {
        requireStep(FStep.SELECT_PARTICIPANTS);
        this.participants = validateParticipants(participants);
        advanceStep();
    }

    private Integer validateParticipants(Integer participants) {
        if (participants == null) throw new IllegalArgumentException("participants cannot be null");
        if (participants < 1) throw new IllegalArgumentException("At least 1 participant is required");
        if (participants > 4) throw new IllegalArgumentException("Maximum 4 participants per booking");
        return participants;
    }

    private void answerQuestion(AnswerEntry answer) {
        requireStep(FStep.ANSWER_QUESTIONS);
        answers.add(validateAnswer(answer));
        if (getCurrentQuestionIndex() == totalQuestions) advanceStep();
    }

    private AnswerEntry validateAnswer(AnswerEntry entry) {
        if (entry.getCode() == null || entry.getCode().isBlank())
            throw new IllegalArgumentException("questionCode cannot be null or blank");
        if (entry.getAnswer() == null || entry.getAnswer().isBlank())
            throw new IllegalArgumentException("answer cannot be null or blank");
        if (getCurrentQuestionIndex() >= totalQuestions)
            throw new IllegalStateException(
                "all questions already answered: %d/%d".formatted(answers.size(), totalQuestions)
            );
        return entry;
    }

    private void selectSeats(BusSeatsPreferences seats) {
        requireStep(FStep.SELECT_BUS_SEATS);
        this.seats = validateSeats(seats);
        advanceStep();
    }

    private BusSeatsPreferences validateSeats(BusSeatsPreferences seats) {
        if (seats == null) throw new IllegalArgumentException("seats cannot be null");
        return seats;
    }

    private void selectAccommodation(AccommodationType acc) {
        requireStep(FStep.SELECT_ACCOMMODATION);
        this.accommodation = validateAccommodation(acc);
        advanceStep();
    }

    private AccommodationType validateAccommodation(AccommodationType acc) {
        if (acc == null) throw new IllegalArgumentException("accommodation cannot be null");
        return acc;
    }

    private void confirm() {
        requireStep(FStep.CONFIRM);

        if (type == null && code == null) {
            throw new IllegalStateException("Either type or code must be set");
        }
        if (dates == null) {
            throw new IllegalStateException("Dates must be selected");
        }
        if (participants == null) {
            throw new IllegalStateException("Participants must be selected");
        }
        if (answers.size() != totalQuestions) {
            throw new IllegalStateException("Not all questions answered");
        }

        advanceStep();
    }

    @JsonIgnore
    public FStep getCurrentStep() {
        return steps.get(currentStepIndex);
    }

    @JsonIgnore
    private int getCurrentQuestionIndex() {
        return answers.size();
    }

    @JsonIgnore
    private void requireStep(FStep expectedStep) {
        final FStep currentStep = getCurrentStep();
        if (currentStep != expectedStep) {
            throw new IllegalStateException(
                "invalid step, expected: %s, but current is: %s".formatted(expectedStep, currentStep)
            );
        }
    }

    @JsonIgnore
    private void advanceStep() {
        if (currentStepIndex >= steps.size() - 1) {
            throw new IllegalStateException("cannot advance: already at the last step");
        }
        currentStepIndex++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BookingSession that = (BookingSession) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}