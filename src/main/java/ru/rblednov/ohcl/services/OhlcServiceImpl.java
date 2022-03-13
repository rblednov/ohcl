package ru.rblednov.ohcl.services;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.quote.QuoteService;

import java.util.List;

@Service
public class OhlcServiceImpl implements OhlcService {
    private final QuoteService quoteService;

    public OhlcServiceImpl(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    public Ohlc getCurrent(long instrumentId, OhlcPeriod period) {
        return quoteService.getCurrent(instrumentId, period);
    }

    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return null;
    }

    public List<Ohlc> getHistoricalAndCurrent(long instrumentId, OhlcPeriod period) {
        return null;
    }

    public void onQuote(Quote quote) {
        quoteService.processQuote(quote);
    }
}
