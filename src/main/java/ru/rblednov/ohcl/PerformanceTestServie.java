package ru.rblednov.ohcl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.OhlcService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ConditionalOnProperty(name = "settings.test", havingValue = "enabled")
@Component
@AllArgsConstructor
@Slf4j
public class PerformanceTestServie {
    private final OhlcService ohlcService;

    @SneakyThrows
    @PostConstruct
    public void init() {
        log.info("performance test starts");
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Object>> todo = new ArrayList<>();
        long a = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            double price = 200d + random.nextInt(800);
            long instrumentId = random.nextInt(100);
            long timestampUtc = System.currentTimeMillis();


            int finalI = i;
            executorService.submit(() -> {
                Quote quote = new QuoteDto(price, instrumentId, timestampUtc);
                if (finalI % 10001 == 0) {
                    quote = new QuoteDto(120d, 3, timestampUtc);
                }
                if (finalI % 10002 ==0) {
                    quote = new QuoteDto(1200, 3, timestampUtc);
                }
                if (finalI % 10003 ==0) {
                    log.info("current ohlc {}" ,ohlcService.getCurrent(3, OhlcPeriod.M1));
                }
                Quote finalQuote = quote;
                ohlcService.onQuote(finalQuote);
            });
            Thread.sleep(1);
        }

        long b = System.currentTimeMillis();
        log.info("performance test takes {}ms", b-a);
    }
}
