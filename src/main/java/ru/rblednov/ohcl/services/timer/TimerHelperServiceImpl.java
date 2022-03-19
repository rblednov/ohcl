package ru.rblednov.ohcl.services.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.historical.HistoricalOhlcHolderService;
import ru.rblednov.ohcl.services.mutex.MutexService;

import static ru.rblednov.ohcl.Utils.samePeriod;

@Service
@Slf4j
public class TimerHelperServiceImpl implements TimerHelperService {
    private final CurrentOhlcHolderService currentOhlcHolderService;
    private final MutexService mutexService;
    private HistoricalOhlcHolderService historicalOhlcService;

    public TimerHelperServiceImpl(CurrentOhlcHolderService currentOhlcHolderService,
                                  MutexService mutexService,
                                  HistoricalOhlcHolderService historicalOhlcService) {
        this.currentOhlcHolderService = currentOhlcHolderService;
        this.mutexService = mutexService;
        this.historicalOhlcService = historicalOhlcService;
    }

    @Override
    public void perform(Quote quote, OhlcPeriod period) {
        synchronized (mutexService.getMutex(quote.getInstrumentId())) {
            Ohlc ohlc = currentOhlcHolderService.getOhcl(quote.getInstrumentId(), period);

            if(ohlc == null){
              log.warn("!!! ohlc == null  {} {} {}", Thread.currentThread().getName(), quote, period);
            } else if (ohlc != null && !samePeriod(period, ohlc.getPeriodStartUtcTimestamp(), System.currentTimeMillis())) {
                historicalOhlcService.store(ohlc);
                currentOhlcHolderService.removeOhlc(quote.getInstrumentId(), period);
            }
        }
    }
}
