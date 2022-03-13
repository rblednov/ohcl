package ru.rblednov.ohcl.services.quote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.CurrentOhlcHolderService;

@Slf4j
@Service
public class QuoteServiceImpl implements QuoteService {
    private final OhlcDao ohlcDao;
    private final CurrentOhlcHolderService currentOhlcHolderService;

    public QuoteServiceImpl(OhlcDao ohlcDao, CurrentOhlcHolderService currentOhlcHolderService) {
        this.ohlcDao = ohlcDao;
        this.currentOhlcHolderService = currentOhlcHolderService;
    }

    public void processQuote(Quote quote) {
        long instrumentId = quote.getInstrumentId();

        /** todo could be parallel*/
        for (OhlcPeriod period : OhlcPeriod.values()) {

            Ohlc currentOhlc = currentOhlcHolderService.getOhcl(instrumentId, period);
            if(currentOhlc == null){
                currentOhlcHolderService.openNewOhcl(quote, period);
            } else if(samePeriod(period, quote, currentOhlc)){
                currentOhlcHolderService.updateCurrentOhlc(period, quote, currentOhlc);
            } else {
                persistCurrentOhlc(currentOhlc.clone());
                currentOhlcHolderService.openNewOhcl(quote, period);
            }
        }
    }

    @Override
    public Ohlc getCurrent(long instrumentId, OhlcPeriod period) {
        return currentOhlcHolderService.getOhcl(instrumentId, period);
    }

    /** todo make
     * Transactional
     * and
     * async*/
    private void persistCurrentOhlc(Ohlc currentOhlc) {
        ohlcDao.store(currentOhlc);
    }

    /**todo switch case is bad practise,
     *  we cannot add new period without new code and recompiling
     *  later we can improve it*/
    private boolean samePeriod(OhlcPeriod period, Quote quote, Ohlc currentOhlc) {
        switch (period){
            case D1:
                return quote.getUtcTimestamp() / (1000 * 60 * 60 * 24) ==
                        currentOhlc.getPeriodStartUtcTimestamp() / (1000 * 60 * 60 * 24);
            case H1:
                return quote.getUtcTimestamp() / (1000 * 60 * 60) ==
                        currentOhlc.getPeriodStartUtcTimestamp() / (1000 * 60 * 60);
            case M1:
                return quote.getUtcTimestamp() / (1000 * 60) ==
                        currentOhlc.getPeriodStartUtcTimestamp() / (1000 * 60);
            default:
                log.error("illegal state of ohlc period");
                throw new IllegalStateException();
        }
    }
}
