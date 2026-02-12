package ru.festtur.telegramdummy.reference.dto.tour;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonPropertyOrder({"start", "end"})
@AllArgsConstructor(staticName = "of")
public class DataRange {
    private LocalDate start;
    private LocalDate end;
}
