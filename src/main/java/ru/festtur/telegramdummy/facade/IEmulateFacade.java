package ru.festtur.telegramdummy.facade;

import ru.festtur.telegramdummy.reference.dto.question.AnswerResult;

public interface IEmulateFacade {
    void loadQuestions();
    String startQuestionnaire();
    AnswerResult processAnswer(String answer);
}
