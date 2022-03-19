package ru.rblednov.ohcl;

import org.junit.jupiter.api.Test;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTest {
    @Test
    public void test() {

        assertTrue(Utils.samePeriod(OhlcPeriod.M1, 815140800000l, 815140800000l));

        assertTrue(Utils.samePeriod(OhlcPeriod.M1, 815140800000l, 815140859000l));
        assertFalse(Utils.samePeriod(OhlcPeriod.M1, 815140800000l, 815140861000l));

        assertTrue(Utils.samePeriod(OhlcPeriod.H1, 815140800000l, 815140861000l));
        assertTrue(Utils.samePeriod(OhlcPeriod.D1, 815140800000l, 815140861000l));

        assertTrue(Utils.samePeriod(OhlcPeriod.H1, 815140800000l, 815144399000l));
        assertFalse(Utils.samePeriod(OhlcPeriod.H1, 815140800000l, 815144400000l));
        assertTrue(Utils.samePeriod(OhlcPeriod.D1, 815140800000l, 815144400000l));

        assertFalse(Utils.samePeriod(OhlcPeriod.M1, 815140800000l, 815184001000l));
        assertFalse(Utils.samePeriod(OhlcPeriod.H1, 815140800000l, 815184001000l));
        assertFalse(Utils.samePeriod(OhlcPeriod.D1, 815140800000l, 815184001000l));
    }
}
