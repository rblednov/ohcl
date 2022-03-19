package ru.rblednov.ohcl.services.historical;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

@Service
public class HistoricalOhlcHolderServiceImpl implements HistoricalOhlcHolderService{
    private final OhlcDao ohlcDao;

    public HistoricalOhlcHolderServiceImpl(OhlcDao ohlcDao) {
        this.ohlcDao = ohlcDao;
    }

    @Override
    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return ohlcDao.getHistorical(instrumentId, period);
    }

    @Override
    public void store(Ohlc ohlc) {
        ohlcDao.store(ohlc);
    }
}
