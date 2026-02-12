package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.client.QuestionApiClient;
import ru.festtur.telegramdummy.reference.dto.AnswerResult;
import ru.festtur.telegramdummy.reference.dto.FullQuestionResponse;
import ru.festtur.telegramdummy.reference.dto.ValidationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmulateFacade implements IEmulateFacade {

    private final QuestionApiClient qApiClient;

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
    public String startQuestionnaire() {
        return QUESTIONS.getFirst().getText();
    }

    @Override
    public AnswerResult processAnswer(final String answer) {
        if (CURRENT_INDEX >= QUESTIONS.size()) {
            return AnswerResult.completed();
        }

        var question = QUESTIONS.get(CURRENT_INDEX);
        final ValidationResult vr = question.getValidation()
            .validate(answer, question.getErrorMessage());

        if (!vr.isValid()) return AnswerResult.error(vr.errorMessage());
        CURRENT_INDEX += 1;

        return CURRENT_INDEX >= QUESTIONS.size()
            ? AnswerResult.completed()
            : AnswerResult.nextQuestion(QUESTIONS.get(CURRENT_INDEX).getText());
    }

}
