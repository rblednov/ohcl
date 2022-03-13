package ru.rblednov.ohcl.services;

import org.springframework.stereotype.Service;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrentOhlcHolderServiceImpl implements CurrentOhlcHolderService {
    private final Map<Long, Ohlc> productToOhclMapM1 = new HashMap<>();
    private final Map<Long, Ohlc> productToOhclMapH1 = new HashMap<>();
    private final Map<Long, Ohlc> productToOhclMapD1 = new HashMap<>();

    @Override
    public void openNewOhcl(Quote quote, OhlcPeriod period) {
        switch (period) {
            case D1:
                productToOhclMapD1.put(quote.getInstrumentId(), new Ohlc(quote, period));
                break;
            case H1:
                productToOhclMapH1.put(quote.getInstrumentId(), new Ohlc(quote, period));
                break;
            case M1:
                productToOhclMapM1.put(quote.getInstrumentId(), new Ohlc(quote, period));
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public Ohlc getOhcl(long instrumentId, OhlcPeriod period) {
        switch (period) {
            case D1:
                return productToOhclMapD1.get(instrumentId);
            case H1:
                return productToOhclMapH1.get(instrumentId);
            case M1:
                return productToOhclMapM1.get(instrumentId);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void updateCurrentOhlc(OhlcPeriod period, Quote quote, Ohlc currentOhlc) {
        switch (period) {
            case D1:
                Ohlc ohlcD1 = productToOhclMapD1.get(quote.getInstrumentId());
                ohlcD1.setHighPrice(Math.max(ohlcD1.getHighPrice(), quote.getPrice()));
                ohlcD1.setLowPrice(Math.min(ohlcD1.getLowPrice(), quote.getPrice()));
                ohlcD1.setClosePrice(quote.getPrice());
                break;
            case H1:
                Ohlc ohlcH2 = productToOhclMapH1.get(quote.getInstrumentId());
                ohlcH2.setHighPrice(Math.max(ohlcH2.getHighPrice(), quote.getPrice()));
                ohlcH2.setLowPrice(Math.min(ohlcH2.getLowPrice(), quote.getPrice()));
                ohlcH2.setClosePrice(quote.getPrice());
                break;
            case M1:
                Ohlc ohlcM1 = productToOhclMapM1.get(quote.getInstrumentId());
                ohlcM1.setHighPrice(Math.max(ohlcM1.getHighPrice(), quote.getPrice()));
                ohlcM1.setLowPrice(Math.min(ohlcM1.getLowPrice(), quote.getPrice()));
                ohlcM1.setClosePrice(quote.getPrice());
                break;
            default:
                throw new IllegalStateException();
        }
    }
}
