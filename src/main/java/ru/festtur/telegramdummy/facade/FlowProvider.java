package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.client.FlowApiClient;
import ru.festtur.telegramdummy.reference.dto.flow.FlowConfig;

@Component
@RequiredArgsConstructor
public class FlowProvider implements IFlowProvider {

    private final FlowApiClient fClient;

    @Override
    public FlowConfig getBase() {
        return fClient.getBaseFlow();
    }

    @Override
    public FlowConfig getShort() {
        return fClient.getShortFlow();
    }

}
