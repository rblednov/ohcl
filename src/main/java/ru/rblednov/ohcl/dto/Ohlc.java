package ru.rblednov.ohcl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Ohlc {
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private final OhlcPeriod ohlcPeriod;
    private final long periodStartUtcTimestamp;

    public Ohlc(double openPrice,
                double highPrice,
                double lowPrice,
                double closePrice,
                OhlcPeriod ohlcPeriod,
                long periodStartUtcTimestamp) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.ohlcPeriod = ohlcPeriod;
        this.periodStartUtcTimestamp = periodStartUtcTimestamp;
    }
}
