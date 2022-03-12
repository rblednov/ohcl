package ru.rblednov.ohcl.services.quote;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final Map<String, Ohlc> currentOhclHolder = new ConcurrentHashMap<String, Ohlc>();

    public void processQuote(Quote quote) {
        long instrumentId = quote.getInstrumentId();

        for (OhlcPeriod period : OhlcPeriod.values()) {
            String key = OhlcPeriod.M1 + String.valueOf(instrumentId);
            Ohlc currentOhlc = currentOhclHolder.getOrDefault(key,
                    Ohlc.builder()
                            .openPrice(quote.getPrice())
                            .highPrice(quote.getPrice())
                            .lowPrice(quote.getPrice())
                            .closePrice(quote.getPrice())
                            .build());
            double qoutePrice = quote.getPrice();
            double ohlcHighPrice = currentOhlc.getHighPrice();
            double ohlcLowPrice = currentOhlc.getLowPrice();
            if (ohlcHighPrice < qoutePrice) {
                currentOhlc.setHighPrice(quote.getPrice());
            }
            if (qoutePrice < ohlcLowPrice) {
                currentOhlc.setLowPrice(qoutePrice);
            }
            currentOhlc.setClosePrice(qoutePrice);
        }
    }
}
