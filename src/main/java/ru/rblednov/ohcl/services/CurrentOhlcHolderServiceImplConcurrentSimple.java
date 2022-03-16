package ru.rblednov.ohcl.services;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.mutex.MutexService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrentOhlcHolderServiceImplConcurrentSimple implements CurrentOhlcHolderService, MutexService {
    private final Map<Long, Map<OhlcPeriod, Ohlc>> holder = new ConcurrentHashMap<>();

    @Override
    public void openNewOhcl(Quote quote, OhlcPeriod period) {
        Map<OhlcPeriod, Ohlc> periodOhlcMap =
                holder.getOrDefault(quote.getInstrumentId(), new HashMap<>());
        periodOhlcMap.put(period, new Ohlc(quote, period));
    }

    @Override
    public Ohlc getOhcl(long instrumentId, OhlcPeriod period) {
        return Optional.ofNullable(holder.get(instrumentId))
                .map(mapPeriod -> mapPeriod.get(period)).orElse(null);
    }

    @Override
    public void updateCurrentOhlc(OhlcPeriod period, Quote quote) {
        Map<OhlcPeriod, Ohlc> periodOhlcMap =
                holder.get(quote.getInstrumentId());

        Ohlc ohlc = periodOhlcMap.get(period);
        ohlc.setHighPrice(Math.max(ohlc.getHighPrice(), quote.getPrice()));
        ohlc.setLowPrice(Math.min(ohlc.getLowPrice(), quote.getPrice()));
        ohlc.setClosePrice(quote.getPrice());
    }

    @Override
    public void removeOhlc(long instrumentId, OhlcPeriod period) {
        Optional.ofNullable(holder.get(instrumentId)).ifPresent(map -> map.remove(period));
    }

    @Override
    public Object getMutex(Quote quote) {

        Map<OhlcPeriod, Ohlc> mutex = holder.get(quote.getInstrumentId());
        if (mutex == null) {
            synchronized (this) {
                mutex = holder.get(quote.getInstrumentId());
                if (mutex == null) {
                    mutex = new HashMap<>();
                    holder.put(quote.getInstrumentId(), mutex);
                    return mutex;
                } else {
                    return mutex;
                }
            }
        }
        return mutex;
    }
}
