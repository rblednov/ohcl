package ru.rblednov.ohcl.services.historical;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.rblednov.ohcl.dao.OhlcDao;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

@Service
public class HistoricalOhlcHolderServiceImpl implements HistoricalOhlcHolderService {
    private final OhlcDao ohlcDao;

    public HistoricalOhlcHolderServiceImpl(OhlcDao ohlcDao) {
        this.ohlcDao = ohlcDao;
    }

    //or no transactional at all, or Propagation.SUPPORTS and readOnly=true
    //but we dont know realisation of OhlcDao
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period) {
        return ohlcDao.getHistorical(instrumentId, period);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void store(Ohlc ohlc) {
        ohlcDao.store(ohlc);
    }
}
