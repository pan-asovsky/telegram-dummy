package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.reference.enums.FlowStep;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlowProvider implements IFlowProvider {

    //todo: получать порядок динамически

    @Override
    public List<FlowStep> getBaseFlow() {
        return List.of(
            FlowStep.SELECT_TYPE,
            FlowStep.SELECT_DESTINATION,
            FlowStep.SELECT_DATES,
            FlowStep.SELECT_PARTICIPANTS,
            FlowStep.ANSWER_QUESTIONS,
            FlowStep.SELECT_BUS_SEATS,
            FlowStep.SELECT_ACCOMMODATION
        );
    }

    @Override
    public List<FlowStep> getAdditionalFlow() {
        return List.of(
            FlowStep.SELECT_CODE,
            FlowStep.SELECT_DATES,
            FlowStep.SELECT_PARTICIPANTS,
            FlowStep.ANSWER_QUESTIONS,
            FlowStep.SELECT_BUS_SEATS,
            FlowStep.SELECT_ACCOMMODATION
        );
    }

}
