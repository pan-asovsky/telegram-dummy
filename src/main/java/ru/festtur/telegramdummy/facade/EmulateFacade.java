package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.client.QuestionApiClient;
import ru.festtur.telegramdummy.client.TourApiClient;
import ru.festtur.telegramdummy.reference.dto.BookingSession;
import ru.festtur.telegramdummy.reference.dto.question.AnswerEntry;
import ru.festtur.telegramdummy.reference.dto.question.AnswerResponse;
import ru.festtur.telegramdummy.reference.dto.question.FullQuestionResponse;
import ru.festtur.telegramdummy.reference.dto.question.ValidationResult;
import ru.festtur.telegramdummy.reference.dto.tour.ShortTourResponse;
import ru.festtur.telegramdummy.reference.dto.tour.TourDates;
import ru.festtur.telegramdummy.reference.dto.tour.TourRequest;
import ru.festtur.telegramdummy.repository.SessionRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmulateFacade implements IEmulateFacade {

    private final SessionRepo sessionRepo;
    private final TourApiClient tApiClient;
    private final QuestionApiClient qApiClient;
    private static final Long DEFAULT_USER_ID = 420L;

    private static final List<FullQuestionResponse> QUESTIONS = new ArrayList<>();
    private static int CURRENT_INDEX = 0;

    @Override
    public void loadQuestions() {
        var sorted = qApiClient.loadQuestions().stream()
            .sorted(Comparator.comparing(FullQuestionResponse::getOrder))
            .toList();
        QUESTIONS.addAll(sorted);
    }

    @Override
    public AnswerResponse start() {
        return AnswerResponse.nextQuestion(QUESTIONS.getFirst().getText());
    }

    @Override
    public AnswerResponse startWithTourCode(final String code) {
        var tourDates = tApiClient.byCode(code)
            .stream()
            .map(ShortTourResponse::getDates)
            .flatMap(List::stream)
            .map(dr -> dr.getStart() + "-" + dr.getEnd())
            .collect(Collectors.joining(" , "));
        return AnswerResponse.nextQuestion(tourDates);
    }

    @Override
    public AnswerResponse processTour(final TourRequest r) {
        var tour = tApiClient.byParams(r);
        sessionRepo.save(
            DEFAULT_USER_ID,
            BookingSession.builder()
                .tourCode(r.getCode())
                .dates(new TourDates(r.getStart(), r.getEnd()))
                .build()
        );
        return AnswerResponse.nextQuestion(tour.toString());
    }

    @Override
    public AnswerResponse processAnswer(final String answer) {
        if (CURRENT_INDEX >= QUESTIONS.size()) return AnswerResponse.completed();

        var question = QUESTIONS.get(CURRENT_INDEX);
        final ValidationResult vr = question.getValidation()
            .validate(answer, question.getErrorMessage());

        if (!vr.isValid()) return AnswerResponse.error(vr.errorMessage());

        sessionRepo.save(
            DEFAULT_USER_ID,
            BookingSession.builder()
                .answers(List.of(new AnswerEntry(question.getCode(), answer)))
                .build()
        );

        CURRENT_INDEX += 1;
        return CURRENT_INDEX >= QUESTIONS.size()
            ? AnswerResponse.completed()
            : AnswerResponse.nextQuestion(QUESTIONS.get(CURRENT_INDEX).getText());
    }

}
