package ru.festtur.telegramdummy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.festtur.telegramdummy.reference.dto.flow.FlowConfig;

@FeignClient(value = "flow-api-client", url = "${clients.core-api.flow.url")
public interface FlowApiClient {

    @GetMapping("/base")
    FlowConfig getBaseFlow();

    @GetMapping("/short")
    FlowConfig getShortFlow();

}
