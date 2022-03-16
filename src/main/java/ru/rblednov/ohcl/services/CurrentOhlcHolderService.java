package ru.rblednov.ohcl.services;

import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;

public interface CurrentOhlcHolderService {
    void openNewOhcl(Quote quote, OhlcPeriod period);

    Ohlc getOhcl(long instrumentId, OhlcPeriod period);

    void updateCurrentOhlc(OhlcPeriod period, Quote quote);

    void removeOhlc(long instrumentId, OhlcPeriod period);
}
