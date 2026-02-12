package ru.festtur.telegramdummy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.festtur.telegramdummy.facade.IEmulateFacade;
import ru.festtur.telegramdummy.reference.dto.question.AnswerResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dummy")
public class UserEmulateController {

    private final IEmulateFacade eFacade;

    @GetMapping("/load-questions")
    public ResponseEntity<Void> loadQuestions() {
        eFacade.loadQuestions();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/start-questionnaire")
    public ResponseEntity<String> startQuestionnaire() {
        return ResponseEntity.ok(eFacade.startQuestionnaire());
    }

    @PostMapping("/process-answer")
    public ResponseEntity<AnswerResult> processAnswer(@RequestParam("answer") String answer) {
        return ResponseEntity.ok(eFacade.processAnswer(answer));
    }

}
