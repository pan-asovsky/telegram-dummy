package ru.festtur.telegramdummy.facade;

import ru.festtur.telegramdummy.reference.dto.flow.FlowConfig;

public interface IFlowProvider {
    FlowConfig getBase();
    FlowConfig getShort();
}
