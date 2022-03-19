package ru.rblednov.ohcl.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderMutexServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(CurrentOhlcHolderMutexServiceImpl.class)
public class CurrentOhlcHolderServiceImplTest {
    @Autowired
    CurrentOhlcHolderService currentOhlcHolderService;

    @Test
    public void testOpenNewGetOhlcEmpty() {

        assertNull(currentOhlcHolderService.getOhcl(3, OhlcPeriod.M1));

        currentOhlcHolderService.openNewOhcl(new QuoteDto(120d, 1, System.currentTimeMillis()), OhlcPeriod.M1);
        assertNull(currentOhlcHolderService.getOhcl(3, OhlcPeriod.M1));
        assertNull(currentOhlcHolderService.getOhcl(1, OhlcPeriod.H1));

        currentOhlcHolderService.removeOhlc(1, OhlcPeriod.M1);
    }

    @Test
    public void testOpenNewGetOhlc() {
        double price = 120d;
        long utcTimestamp = 815140800000l;

        currentOhlcHolderService.openNewOhcl(new QuoteDto(price, 1, utcTimestamp), OhlcPeriod.M1);
        Ohlc ohlc = currentOhlcHolderService.getOhcl(1, OhlcPeriod.M1);
        assertEquals(OhlcPeriod.M1, ohlc.getOhlcPeriod());
        assertEquals(price, ohlc.getOpenPrice());

        currentOhlcHolderService.removeOhlc(1, OhlcPeriod.M1);
    }

    @Test
    public void testUpdateOhcl() {
        double openPrice = 120d;
        double highPrice = 140d;
        double lowPrice = 110d;
        long utcTimestamp = 815140800000l;

        currentOhlcHolderService.openNewOhcl(new QuoteDto(openPrice, 1, utcTimestamp), OhlcPeriod.M1);
        currentOhlcHolderService.updateCurrentOhlc(OhlcPeriod.M1, new QuoteDto(highPrice, 1, utcTimestamp + 1));
        currentOhlcHolderService.updateCurrentOhlc(OhlcPeriod.M1, new QuoteDto(lowPrice, 1, utcTimestamp + 2));

        Ohlc ohlc = currentOhlcHolderService.getOhcl(1, OhlcPeriod.M1);

        assertEquals(OhlcPeriod.M1, ohlc.getOhlcPeriod());
        assertEquals(openPrice, ohlc.getOpenPrice());
        assertEquals(lowPrice, ohlc.getLowPrice());

        currentOhlcHolderService.removeOhlc(1, OhlcPeriod.M1);
    }

    @Test
    public void testRemoveOhcl() {
        double openPrice = 120d;
        long utcTimestamp = 815140800000l;

        currentOhlcHolderService.openNewOhcl(new QuoteDto(openPrice, 1, utcTimestamp), OhlcPeriod.M1);


        Ohlc ohlc = currentOhlcHolderService.getOhcl(1, OhlcPeriod.M1);

        assertEquals(OhlcPeriod.M1, ohlc.getOhlcPeriod());
        assertEquals(openPrice, ohlc.getOpenPrice());

        currentOhlcHolderService.removeOhlc(1, OhlcPeriod.M1);
        assertNull(currentOhlcHolderService.getOhcl(1, OhlcPeriod.H1));
    }
}
