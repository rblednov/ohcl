package ru.rblednov.ohcl.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class Ohlc implements Cloneable {
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

    public Ohlc(Quote quote, OhlcPeriod period) {
        this.openPrice = quote.getPrice();
        this.highPrice = quote.getPrice();
        this.lowPrice = quote.getPrice();
        this.closePrice = quote.getPrice();
        this.ohlcPeriod = period;
        this.periodStartUtcTimestamp = quote.getUtcTimestamp();
    }

    @SneakyThrows
    @Override
    public Ohlc clone() {
        return (Ohlc) super.clone();
    }
}
