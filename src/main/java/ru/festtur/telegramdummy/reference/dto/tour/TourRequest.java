package ru.festtur.telegramdummy.reference.dto.tour;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TourRequest {
    @NotEmpty private String code;
    @NotNull private LocalDate start;
    @NotNull private LocalDate end;
}
