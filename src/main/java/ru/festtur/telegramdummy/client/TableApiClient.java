package ru.festtur.telegramdummy.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "table-api-client", url = "${clients.core-api.table.url}")
public interface TableApiClient {
}
