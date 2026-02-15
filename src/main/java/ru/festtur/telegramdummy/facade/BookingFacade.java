package ru.festtur.telegramdummy.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.festtur.telegramdummy.repository.SessionRepo;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingFacade implements IBookingFacade {

    private final SessionRepo sessionRepo;

    @Override
    public void start() {

    }

    @Override
    public void startWithCode() {

    }

}
