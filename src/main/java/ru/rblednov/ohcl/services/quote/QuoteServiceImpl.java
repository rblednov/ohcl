package ru.rblednov.ohcl.services.quote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.historical.HistoricalOhlcHolderService;
import ru.rblednov.ohcl.services.timer.TimerHelperService;
import ru.rblednov.ohcl.services.mutex.QuoteTimerTask;

import java.util.Timer;
import java.util.TimerTask;

import static ru.rblednov.ohcl.Utils.samePeriod;

@Slf4j
@Service
public class QuoteServiceImpl implements QuoteService {

    private final CurrentOhlcHolderService currentOhlcHolderService;
    private final TimerHelperService timerHelperService;
    private final HistoricalOhlcHolderService historicalOhlcHolder;

    public QuoteServiceImpl(CurrentOhlcHolderService currentOhlcHolderService,
                            TimerHelperService timerHelperService,
                            HistoricalOhlcHolderService historicalOhlcHolder) {
        this.currentOhlcHolderService = currentOhlcHolderService;
        this.timerHelperService = timerHelperService;
        this.historicalOhlcHolder = historicalOhlcHolder;
    }

    public void processQuote(Quote quote) {
        long instrumentId = quote.getInstrumentId();

        for (OhlcPeriod period : OhlcPeriod.values()) {

            Ohlc currentOhlc = currentOhlcHolderService.getOhcl(instrumentId, period);
            if (currentOhlc == null) {
                currentOhlcHolderService.openNewOhcl(quote, period);
                setTimer(quote, period, timerHelperService);
            } else if (samePeriod(period, quote.getUtcTimestamp(), currentOhlc.getPeriodStartUtcTimestamp())) {
                currentOhlcHolderService.updateCurrentOhlc(period, quote);
            } else {
                persistCurrentOhlc(currentOhlc.clone());
                currentOhlcHolderService.openNewOhcl(quote, period);
                setTimer(quote, period, timerHelperService);
            }
        }
    }

    private void setTimer(Quote quote, OhlcPeriod period, TimerHelperService timerHelperService) {
        Timer timer = new Timer(true);
        TimerTask timerTask = new QuoteTimerTask(quote, period, timerHelperService);
        long delay = 0;

        /** to prevent cases when some qoute was late */
        long deltaDelay = 100;

        switch (period) {
            case M1:
                delay = 1000 * 60 + deltaDelay - quote.getUtcTimestamp() % (1000 * 60);
                break;
            case H1:
                delay = 1000 * 60 * 60 + deltaDelay - quote.getUtcTimestamp() % (1000 * 60 * 60);
                break;
            case D1:
                delay = 1000 * 60 * 60 * 24 + deltaDelay - quote.getUtcTimestamp() % (1000 * 60 * 60 * 24);
                break;
            default:
                throw new IllegalStateException();
        }
        timer.schedule(timerTask, delay);
    }

    @Override
    public Ohlc getCurrent(long instrumentId, OhlcPeriod period) {
        return currentOhlcHolderService.getOhcl(instrumentId, period);
    }

    private void persistCurrentOhlc(Ohlc currentOhlc) {
        historicalOhlcHolder.store(currentOhlc);
    }
}
