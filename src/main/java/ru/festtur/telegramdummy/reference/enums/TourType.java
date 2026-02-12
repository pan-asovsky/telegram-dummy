package ru.festtur.telegramdummy.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TourType {
    LONG_RANGE_SKI("LS"),
    AUTHOR("AU"),
    RAFTING_HIKING("RH"),
    WEEKEND_SKI("WS"),
    NEW_YEAR("NY");

    @Getter
    private final String code;

}