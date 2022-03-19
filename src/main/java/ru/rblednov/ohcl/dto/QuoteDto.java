package ru.rblednov.ohcl.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class QuoteDto implements Quote {
    private final double price;
    private final long instrumentId;
    private final long utcTimestamp;

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public long getInstrumentId() {
        return instrumentId;
    }

    @Override
    public long getUtcTimestamp() {
        return utcTimestamp;
    }
}
