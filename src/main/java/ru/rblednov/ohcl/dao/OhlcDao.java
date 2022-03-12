package ru.rblednov.ohcl.dao;

import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

interface OhlcDao {
    void store(Ohlc ohlc);
    /** loads OHLCs from DB selected by parameters and sorted by
     periodStartUtcTimestamp in descending order */
    List<Ohlc> getHistorical (long instrumentId, OhlcPeriod period);
}