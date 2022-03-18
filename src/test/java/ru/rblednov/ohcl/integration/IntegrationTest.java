package ru.rblednov.ohcl.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dao.OhlcDaoImpl;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.OhlcService;
import ru.rblednov.ohcl.services.OhlcServiceImpl;
import ru.rblednov.ohcl.services.TimerHelperService;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderServiceImpl;
import ru.rblednov.ohcl.services.quote.QuoteServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({OhlcDaoImpl.class,
        OhlcServiceImpl.class,
        QuoteServiceImpl.class,
        CurrentOhlcHolderServiceImpl.class,
        TimerHelperService.class})
public class IntegrationTest {
    @Autowired
    OhlcService ohlcService;

    @Test
    public void testSimple() {
        long instrumentId = 3;
        double price = 120d;
        long timestampUtc = System.currentTimeMillis();

        ohlcService.onQuote(new QuoteDto(price, instrumentId, timestampUtc));

        Ohlc ohlcM1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.M1);
        Ohlc ohlcH1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.H1);
        Ohlc ohlcD1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.D1);

        assertEquals(timestampUtc, ohlcM1.getPeriodStartUtcTimestamp());
        assertEquals(timestampUtc, ohlcH1.getPeriodStartUtcTimestamp());
        assertEquals(timestampUtc, ohlcD1.getPeriodStartUtcTimestamp());

        assertEquals(price, ohlcM1.getOpenPrice());
        assertEquals(price, ohlcH1.getOpenPrice());
        assertEquals(price, ohlcD1.getOpenPrice());

        assertEquals(price, ohlcM1.getHighPrice());
        assertEquals(price, ohlcH1.getHighPrice());
        assertEquals(price, ohlcD1.getHighPrice());

        assertEquals(price, ohlcM1.getClosePrice());
        assertEquals(price, ohlcH1.getClosePrice());
        assertEquals(price, ohlcD1.getClosePrice());

        assertEquals(price, ohlcM1.getLowPrice());
        assertEquals(price, ohlcH1.getLowPrice());
        assertEquals(price, ohlcD1.getLowPrice());
    }

    @Test
    public void testMedium() {
        long instrumentId = 3;
        long timestampUtcFirst = System.currentTimeMillis();
        Quote quote1 = new QuoteDto(120, instrumentId, timestampUtcFirst);
        Quote quote2 = new QuoteDto(140, instrumentId, System.currentTimeMillis());
        Quote quote3 = new QuoteDto(110, instrumentId, System.currentTimeMillis());
        Quote quote4 = new QuoteDto(130, instrumentId, System.currentTimeMillis());

        ohlcService.onQuote(quote1);
        ohlcService.onQuote(quote2);
        ohlcService.onQuote(quote3);
        ohlcService.onQuote(quote4);

        Ohlc ohlcM1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.M1);
        Ohlc ohlcH1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.H1);
        Ohlc ohlcD1 = ohlcService.getCurrent(instrumentId, OhlcPeriod.D1);

        assertEquals(timestampUtcFirst, ohlcM1.getPeriodStartUtcTimestamp());
        assertEquals(timestampUtcFirst, ohlcH1.getPeriodStartUtcTimestamp());
        assertEquals(timestampUtcFirst, ohlcD1.getPeriodStartUtcTimestamp());

        assertEquals(quote1.getPrice(), ohlcM1.getOpenPrice());
        assertEquals(quote1.getPrice(), ohlcH1.getOpenPrice());
        assertEquals(quote1.getPrice(), ohlcD1.getOpenPrice());

        assertEquals(quote2.getPrice(), ohlcM1.getHighPrice());
        assertEquals(quote2.getPrice(), ohlcH1.getHighPrice());
        assertEquals(quote2.getPrice(), ohlcD1.getHighPrice());

        assertEquals(quote3.getPrice(), ohlcM1.getLowPrice());
        assertEquals(quote3.getPrice(), ohlcH1.getLowPrice());
        assertEquals(quote3.getPrice(), ohlcD1.getLowPrice());

        assertEquals(quote4.getPrice(), ohlcM1.getClosePrice());
        assertEquals(quote4.getPrice(), ohlcH1.getClosePrice());
        assertEquals(quote4.getPrice(), ohlcD1.getClosePrice());
    }
}
