package ru.festtur.telegramdummy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.festtur.telegramdummy.reference.dto.FullQuestionResponse;

import java.util.List;

@FeignClient(value = "question-api-client", url = "${clients.core-api.questions.url}")
public interface QuestionApiClient {
    @GetMapping
    List<FullQuestionResponse> loadQuestions();
}
