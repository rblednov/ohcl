package ru.rblednov.ohcl.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dao.OhlcDaoImpl;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.historical.HistoricalOhlcHolderService;
import ru.rblednov.ohcl.services.mutex.MutexService;
import ru.rblednov.ohcl.services.quote.QuoteService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({OhlcServiceImpl.class})
public class OhlcServiceTest {
    @Autowired
    OhlcService ohlcService;
    @MockBean
    QuoteService quoteService;
    @MockBean
    MutexService mutexService;
    @MockBean
    CurrentOhlcHolderService currentOhlcHolderService;
    @MockBean
    HistoricalOhlcHolderService historicalOhlcHolderService;

    @SneakyThrows
    @Test
    public void testSinglePositiveOnQuoteConcurrentLock(){
        AtomicInteger integer = new AtomicInteger(0);
        doAnswer(invocation -> {
            System.out.println("### do logic " + Thread.currentThread().getName());
            integer.incrementAndGet();
            Thread.sleep(5000);
            return null;
        }).when(quoteService).processQuote(any());

        Quote quote1 = new QuoteDto(120, 3, System.currentTimeMillis());
        Quote quote2 = new QuoteDto(140, 3, System.currentTimeMillis());
        Object mutexObj = new Object();

        when(mutexService.getMutex(anyLong())).thenReturn(mutexObj);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(()-> ohlcService.onQuote(quote1));
        executorService.submit(()-> ohlcService.onQuote(quote2));
        Thread.sleep(50);
        assertEquals(1,integer.get());
    }

    @SneakyThrows
    @Test
    public void testSinglePositiveOnQuoteConcurrentNoLock(){
        AtomicInteger integer = new AtomicInteger(0);
        doAnswer(invocation -> {
            System.out.println("### do logic " + Thread.currentThread().getName());
            integer.incrementAndGet();
            Thread.sleep(5000);
            return null;
        }).when(quoteService).processQuote(any());

        Quote quote1 = new QuoteDto(120, 3, System.currentTimeMillis());
        Quote quote2 = new QuoteDto(140, 4, System.currentTimeMillis());
        Object mutexObj1 = new Object();
        Object mutexObj2 = new Object();

        when(mutexService.getMutex(quote1.getInstrumentId())).thenReturn(mutexObj1);
        when(mutexService.getMutex(quote2.getInstrumentId())).thenReturn(mutexObj2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(()-> ohlcService.onQuote(quote1));
        executorService.submit(()-> ohlcService.onQuote(quote2));
        Thread.sleep(50);
        assertEquals(2,integer.get());
    }
}
