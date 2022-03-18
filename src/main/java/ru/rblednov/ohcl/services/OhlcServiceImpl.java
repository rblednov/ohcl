package ru.rblednov.ohcl.services;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.historical.HistoricalOhlcHolderService;
import ru.rblednov.ohcl.services.mutex.MutexService;
import ru.rblednov.ohcl.services.quote.QuoteService;

import java.util.List;

@Service
public class OhlcServiceImpl implements OhlcService {
    private final QuoteService quoteService;
    private final MutexService mutexService;
    private CurrentOhlcHolderService currentOhlcHolder;
    private HistoricalOhlcHolderService historicalOhlcHolder;

    public OhlcServiceImpl(QuoteService quoteService, MutexService mutexService, CurrentOhlcHolderService currentOhlcHolder, HistoricalOhlcHolderService historicalOhlcHolder) {
        this.quoteService = quoteService;
        this.mutexService = mutexService;
        this.currentOhlcHolder = currentOhlcHolder;
        this.historicalOhlcHolder = historicalOhlcHolder;
    }

    public Ohlc getCurrent(long instrumentId, OhlcPeriod period) {
        return currentOhlcHolder.getOhcl(instrumentId, period);
    }

    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return historicalOhlcHolder.getHistorical(instrumentId, period);
    }

    public List<Ohlc> getHistoricalAndCurrent(long instrumentId, OhlcPeriod period) {
        List<Ohlc> ohlcList = historicalOhlcHolder.getHistorical(instrumentId, period);
        ohlcList.add(currentOhlcHolder.getOhcl(instrumentId, period));
        return ohlcList;
    }

    public void onQuote(Quote quote) {
        synchronized (mutexService.getMutex(quote)) {
            quoteService.processQuote(quote);
        }
    }
}
