package ru.rblednov.ohcl.services.historical;

import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.List;

public interface HistoricalOhlcHolderService {
    List<Ohlc> getHistorical(long instrumentId, OhlcPeriod period);
}
