package ru.festtur.telegramdummy.reference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.festtur.telegramdummy.reference.dto.question.AnswerEntry;
import ru.festtur.telegramdummy.reference.dto.tour.TourDates;
import ru.festtur.telegramdummy.reference.enums.AccommodationType;
import ru.festtur.telegramdummy.reference.enums.BusSeatsPreferences;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSession {
    private String tourCode;
    private TourDates dates;
    private Integer participants;
    @Builder.Default
    private List<AnswerEntry> answers = new ArrayList<>();
    private Integer currentQuestionIndex;
    private BusSeatsPreferences seats;
    private AccommodationType placement;
}
