package ru.rblednov.ohcl.dto;

public interface Quote {
    double getPrice();
    long getInstrumentId();
    long getUtcTimestamp();
}