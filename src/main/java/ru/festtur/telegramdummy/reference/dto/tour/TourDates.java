package ru.festtur.telegramdummy.reference.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TourDates {
    private LocalDate start;
    private LocalDate end;
}
