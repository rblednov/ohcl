package ru.rblednov.ohcl.services.timer;

import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;

public interface TimerHelperService  {
    void perform(Quote quote, OhlcPeriod period);
}
