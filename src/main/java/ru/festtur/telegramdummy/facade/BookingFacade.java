package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.reference.dto.BookingSession;
import ru.festtur.telegramdummy.repository.SessionRepo;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingFacade implements IBookingFacade {

    private final SessionRepo sessionRepo;
    private final IFlowProvider flowProvider;

    private static final Long USER_ID = 420L;

    @Override
    public void start() {
        var flow = flowProvider.getBase();
        var session = BookingSession.start(USER_ID, flow);
        var current = session.getCurrentStep();
    }

    @Override
    public void start(String code) {
        var flow = flowProvider.getShort();
        var session = BookingSession.startFromCode(USER_ID, flow, code);
        sessionRepo.save(USER_ID, session);

    }

}
