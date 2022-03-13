package ru.rblednov.ohcl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dao.OhlcDaoImpl;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.QuoteDto;
import ru.rblednov.ohcl.services.CurrentOhlcHolderService;
import ru.rblednov.ohcl.services.CurrentOhlcHolderServiceImpl;
import ru.rblednov.ohcl.services.OhlcService;
import ru.rblednov.ohcl.services.OhlcServiceImpl;
import ru.rblednov.ohcl.services.quote.QuoteServiceImpl;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({OhlcDaoImpl.class, OhlcServiceImpl.class, QuoteServiceImpl.class, CurrentOhlcHolderServiceImpl.class})
public class OhlcServiceTest {
    @Autowired
    OhlcServiceImpl ohlcService;


    @Test
    public void test() throws InterruptedException {

        Random random = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            Thread.sleep(1);
            if (i == 10000) {
                ohlcService.onQuote(new QuoteDto(
                        120,
                        1,
                        System.currentTimeMillis()));
            } else if (i == 20000) {
                ohlcService.onQuote(new QuoteDto(
                        1200,
                        1,
                        System.currentTimeMillis()));
            } else if (i == 30000 || i == 50000 || i== 70000) {
                System.out.println("## " + ohlcService.getCurrent(1, OhlcPeriod.M1));
                System.out.println("## " + ohlcService.getCurrent(2, OhlcPeriod.M1));
                System.out.println("## " + ohlcService.getCurrent(3, OhlcPeriod.M1));
            } else {
                ohlcService.onQuote(new QuoteDto(
                        random.nextInt(800) + 200,
                        random.nextInt(100),
                        System.currentTimeMillis()));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start + " millis");
        System.out.println((end - start)/1000 + " seconds");
    }
}
