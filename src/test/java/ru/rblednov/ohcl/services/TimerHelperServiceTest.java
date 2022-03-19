package ru.rblednov.ohcl.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.historical.HistoricalOhlcHolderService;
import ru.rblednov.ohcl.services.mutex.MutexService;
import ru.rblednov.ohcl.services.timer.TimerHelperService;
import ru.rblednov.ohcl.services.timer.TimerHelperServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({TimerHelperServiceImpl.class})
public class TimerHelperServiceTest {
    @Autowired
    TimerHelperService timerHelperService;
    @MockBean
    MutexService mutexService;
    @MockBean
    CurrentOhlcHolderService currentOhlcHolderService;
    @MockBean
    HistoricalOhlcHolderService historicalOhlcHolderService;

    @Test
    public void testSamePeriod() {
        Quote quote = new QuoteDto(120d, 3, System.currentTimeMillis());
        Ohlc ohlc = new Ohlc(quote, OhlcPeriod.M1);
        Object mutexObj = new Object();


        when(mutexService.getMutex(quote.getInstrumentId())).thenReturn(mutexObj);
        when(currentOhlcHolderService.getOhcl(3, OhlcPeriod.M1)).thenReturn(ohlc);

        timerHelperService.perform(quote, OhlcPeriod.M1);
        verifyNoInteractions(historicalOhlcHolderService);
    }

    @Test
    public void testAnotherPeriod() {
        Quote quote = new QuoteDto(120d, 3, 815140800000l);
        Ohlc ohlc = new Ohlc(quote, OhlcPeriod.M1);
        Object mutexObj = new Object();


        when(mutexService.getMutex(quote.getInstrumentId())).thenReturn(mutexObj);
        when(currentOhlcHolderService.getOhcl(3, OhlcPeriod.M1)).thenReturn(ohlc);

        timerHelperService.perform(quote, OhlcPeriod.M1);
        verify(historicalOhlcHolderService).store(ohlc);
    }
}
