package ru.festtur.telegramdummy.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.festtur.telegramdummy.reference.dto.tour.FullTourResponse;
import ru.festtur.telegramdummy.reference.dto.tour.ShortTourResponse;
import ru.festtur.telegramdummy.reference.dto.tour.TourRequest;
import ru.festtur.telegramdummy.reference.enums.TourType;

import java.util.List;

@FeignClient(value = "tour-api-client", url = "${clients.core-api.tours.url}")
public interface TourApiClient {

    @GetMapping("/by-code")
    List<ShortTourResponse> byCode(@RequestParam("code") String code);

    @GetMapping("/types")
    List<TourType> getActiveTypes();

    @GetMapping("/by-type")
    List<ShortTourResponse> byType(@RequestParam("type") TourType type);

    @PostMapping("/by-params")
    FullTourResponse byParams(@RequestBody @Valid TourRequest request);

}
