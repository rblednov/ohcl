package ru.rblednov.ohcl.services;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.mutex.MutexService;
import ru.rblednov.ohcl.services.quote.QuoteService;

import static ru.rblednov.ohcl.Utils.samePeriod;

@Service
public class TimerHelperService {
    private final CurrentOhlcHolderService currentOhlcHolderService;
    private final OhlcDao ohlcDao;
    private final MutexService mutexService;

    public TimerHelperService(CurrentOhlcHolderService currentOhlcHolderService,
                              OhlcDao ohlcDao,
                              MutexService mutexService) {
        this.currentOhlcHolderService = currentOhlcHolderService;
        this.ohlcDao = ohlcDao;
        this.mutexService = mutexService;
    }

    public void perform(Quote quote, OhlcPeriod period) {
        synchronized (mutexService.getMutex(quote)) {
            Ohlc ohlc = currentOhlcHolderService.getOhcl(quote.getInstrumentId(), period);
            if(! samePeriod(period, ohlc.getPeriodStartUtcTimestamp(), System.currentTimeMillis())){
                if (ohlc!=null){
                    ohlcDao.store(ohlc);
                }
                currentOhlcHolderService.removeOhlc(quote.getInstrumentId(), period);
            }
        }
    }
}
