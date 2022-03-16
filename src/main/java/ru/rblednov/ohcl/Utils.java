package ru.rblednov.ohcl;

import lombok.extern.slf4j.Slf4j;
import ru.rblednov.ohcl.dto.OhlcPeriod;

@Slf4j
public class Utils {
    /**
     * todo switch case is bad practise,
     *  we cannot add new period without new code and recompiling
     *  later we can improve it
     */
    public static boolean samePeriod(OhlcPeriod period, long quoteTimestamp, long ohlcTimeStamp) {
        switch (period) {
            case D1:
                return quoteTimestamp / (1000 * 60 * 60 * 24) ==
                        ohlcTimeStamp / (1000 * 60 * 60 * 24);
            case H1:
                return quoteTimestamp / (1000 * 60 * 60) ==
                        ohlcTimeStamp / (1000 * 60 * 60);
            case M1:
                return quoteTimestamp / (1000 * 60) ==
                        ohlcTimeStamp / (1000 * 60);
            default:
                log.error("illegal state of ohlc period");
                throw new IllegalStateException();
        }
    }
}
