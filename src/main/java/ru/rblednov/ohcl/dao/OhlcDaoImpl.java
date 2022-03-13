package ru.rblednov.ohcl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

@Slf4j
@Service
public class OhlcDaoImpl implements OhlcDao{
    @Override
    public void store(Ohlc ohlc) {
        log.info("ohcl stored {}", ohlc);
    }

    @Override
    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return null;
    }
}
