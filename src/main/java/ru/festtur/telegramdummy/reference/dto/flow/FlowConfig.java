package ru.festtur.telegramdummy.reference.dto.flow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.festtur.telegramdummy.reference.dto.question.FullQuestionResponse;
import ru.festtur.telegramdummy.reference.enums.FlowType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlowConfig {
    private FlowType type;
    private List<FlowStep> steps;
    private List<FullQuestionResponse> questions;
    private LocalDateTime capturedAt;

    public List<FullQuestionResponse> getQuestions() {
        return questions.stream()
            .sorted(Comparator.comparingInt(FullQuestionResponse::getOrder))
            .toList();
    }

}
