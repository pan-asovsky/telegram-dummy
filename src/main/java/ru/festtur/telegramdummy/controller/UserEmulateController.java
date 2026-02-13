package ru.festtur.telegramdummy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.festtur.telegramdummy.facade.IEmulateFacade;
import ru.festtur.telegramdummy.reference.dto.question.AnswerResponse;
import ru.festtur.telegramdummy.reference.dto.tour.TourRequest;

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

    @GetMapping("/start")
    public ResponseEntity<AnswerResponse> start() {
        return ResponseEntity.ok(eFacade.start());
    }

    @GetMapping("/start/{code}")
    public ResponseEntity<AnswerResponse> startWithTourCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(eFacade.startWithTourCode(code));
    }

    @GetMapping("/process-tour")
    public ResponseEntity<AnswerResponse> processTour(@RequestBody TourRequest r) {
        return ResponseEntity.ok(eFacade.processTour(r));
    }

    @PostMapping("/process-answer")
    public ResponseEntity<AnswerResponse> processAnswer(@RequestParam("answer") String answer) {
        return ResponseEntity.ok(eFacade.processAnswer(answer));
    }

}
