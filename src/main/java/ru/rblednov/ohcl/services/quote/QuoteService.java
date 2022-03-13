package ru.rblednov.ohcl.services.quote;

import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;

public interface QuoteService {
    void processQuote(Quote quote);

    Ohlc getCurrent(long instrumentId, OhlcPeriod period);
}
