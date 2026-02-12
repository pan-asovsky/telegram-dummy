package ru.festtur.telegramdummy.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "tour-api-client", url = "${clients.core-api.tours.url}")
public interface TourApiClient {
}
