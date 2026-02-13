package ru.festtur.telegramdummy.facade;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import ru.festtur.telegramdummy.reference.dto.question.AnswerResponse;
import ru.festtur.telegramdummy.reference.dto.tour.TourRequest;

@Validated
public interface IEmulateFacade {
    void loadQuestions();
    AnswerResponse start();
    AnswerResponse startWithTourCode(@NotEmpty String code);
    AnswerResponse processTour(@Valid TourRequest r);
    AnswerResponse processAnswer(String answer);
}
