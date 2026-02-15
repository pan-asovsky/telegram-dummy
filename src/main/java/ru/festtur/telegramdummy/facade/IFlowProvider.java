package ru.festtur.telegramdummy.facade;

import ru.festtur.telegramdummy.reference.enums.FlowStep;

import java.util.List;

public interface IFlowProvider {
    List<FlowStep> getBaseFlow();
    List<FlowStep> getAdditionalFlow();
}
