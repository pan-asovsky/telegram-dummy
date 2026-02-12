package ru.festtur.telegramdummy.reference.dto.tour;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.festtur.telegramdummy.reference.enums.TourType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@JsonPropertyOrder({"code", "type", "direction", "dates"})
public class ShortTourResponse {
    private String code;
    private TourType type;
    private String direction;
    private List<DataRange> dates;
}
