package ru.festtur.telegramdummy.reference.dto.tour;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.festtur.telegramdummy.reference.enums.TourType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class FullTourResponse {
    private String code;
    @JsonProperty("active")
    private boolean isActive;
    private TourType type;
    private String direction;
    private boolean canChoicePlacement;
    private boolean canChoiceBusSeats;
    private BigDecimal standardPrice;
    private BigDecimal placementPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}
