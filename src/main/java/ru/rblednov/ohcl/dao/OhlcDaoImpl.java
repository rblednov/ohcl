package ru.rblednov.ohcl.dao;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

@Slf4j
@Service
public class OhlcDaoImpl implements OhlcDao {
    @SneakyThrows
    @Override
    public void store(Ohlc ohlc) {
        long a = System.nanoTime();
        while (System.nanoTime() - a < 100) {

        }
        log.info("ohlc stored {} {}", Thread.currentThread().getName(), ohlc);
    }

    @Override
    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return null;
    }
}
