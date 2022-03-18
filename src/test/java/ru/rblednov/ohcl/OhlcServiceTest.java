package ru.rblednov.ohcl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dao.OhlcDaoImpl;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderServiceImpl;
import ru.rblednov.ohcl.services.OhlcServiceImpl;
import ru.rblednov.ohcl.services.TimerHelperService;
import ru.rblednov.ohcl.services.quote.QuoteServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({OhlcDaoImpl.class,
        OhlcServiceImpl.class,
        QuoteServiceImpl.class,
        CurrentOhlcHolderServiceImpl.class,
        TimerHelperService.class})
public class OhlcServiceTest {
    @Autowired
    OhlcServiceImpl ohlcService;
    @MockBean
    QuoteServiceImpl quoteService;

    @SneakyThrows
    @Test
    public void testSinglePositiveOnQuoteConcurentLock(){
        AtomicInteger integer = new AtomicInteger(0);
        doAnswer(invocation -> {
            System.out.println("### do logic " + Thread.currentThread().getName());
            integer.incrementAndGet();
            Thread.sleep(5000);
            return null;
        }).when(quoteService).processQuote(any());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(()-> ohlcService.onQuote(new QuoteDto(120, 3, System.currentTimeMillis())));
        executorService.submit(()-> ohlcService.onQuote(new QuoteDto(140, 3, System.currentTimeMillis())));
        Thread.sleep(50);
        assertEquals(1,integer.get());
    }

    @SneakyThrows
    @Test
    public void testSinglePositiveOnQuoteConcurentNoLock(){
        AtomicInteger integer = new AtomicInteger(0);
        doAnswer(invocation -> {
            System.out.println("### do logic " + Thread.currentThread().getName());
            integer.incrementAndGet();
            Thread.sleep(5000);
            return null;
        }).when(quoteService).processQuote(any());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(()-> ohlcService.onQuote(new QuoteDto(120, 3, System.currentTimeMillis())));
        executorService.submit(()-> ohlcService.onQuote(new QuoteDto(140, 4, System.currentTimeMillis())));
        Thread.sleep(50);
        assertEquals(2  ,integer.get());
    }
//
//    @Test
//    public void test1() throws InterruptedException {
//
//        Random random = new Random();
//        long start = System.currentTimeMillis();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<Callable<Object>> todo = new ArrayList<>();
//        for(int i = 0;i<10;i++){
//            todo.add(Executors.callable(()->{
//                for (int j = 0; j < 30000; j++) {
////            Thread.sleep(1);
//                    if (j == 10000) {
//                        ohlcService.onQuote(new QuoteDto(
//                                120,
//                                1,
//                                System.currentTimeMillis()));
//                    } else if (j == 20000) {
//                        ohlcService.onQuote(new QuoteDto(
//                                1200,
//                                1,
//                                System.currentTimeMillis()));
//                    } else if (j == 30000 || j == 50000 || j == 70000) {
//                        System.out.println("## " + ohlcService.getCurrent(1, OhlcPeriod.M1));
//                        System.out.println("## " + ohlcService.getCurrent(2, OhlcPeriod.M1));
//                        System.out.println("## " + ohlcService.getCurrent(3, OhlcPeriod.M1));
//                    } else {
//                        ohlcService.onQuote(new QuoteDto(
//                                random.nextInt(800) + 200,
//                                random.nextInt(100),
//                                System.currentTimeMillis()));
//                    }
//                }
//            }));
//        }
//        executorService.invokeAll(todo);
//        long end = System.currentTimeMillis();
//        System.out.println(end - start + " millis");
//        System.out.println((end - start) / 1000 + " seconds");
//        Thread.sleep(80000);
//    }
//
//
//    @Test
//    public void test2() throws InterruptedException {
//
//        Random random = new Random();
//        ohlcService.onQuote(new QuoteDto(120, 3, System.currentTimeMillis()));
//        System.out.println(ohlcService.getCurrent(3, OhlcPeriod.M1));
//        Thread.sleep(60000);
//        System.out.println(ohlcService.getCurrent(3, OhlcPeriod.M1));
//        ohlcService.onQuote(new QuoteDto(140, 3, System.currentTimeMillis()));
//        System.out.println(ohlcService.getCurrent(3, OhlcPeriod.M1));
//        Thread.sleep(80000);
//        System.out.println(ohlcService.getCurrent(3, OhlcPeriod.M1));
//        System.out.println(ohlcService.getCurrent(3, OhlcPeriod.M1));
//
//    }
}
