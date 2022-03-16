package ru.rblednov.ohcl.services.mutex;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.rblednov.ohcl.dto.OhlcPeriod;
import ru.rblednov.ohcl.dto.Quote;
import ru.rblednov.ohcl.services.TimerHelperService;

import java.util.TimerTask;

public class QuoteTimerTask extends TimerTask {
    private final Quote quote;
    private final OhlcPeriod period;
    private TimerHelperService timerHelperService;

    public QuoteTimerTask(Quote quote, OhlcPeriod period, TimerHelperService timerHelperService) {
        this.quote = quote;
        this.period = period;
        this.timerHelperService = timerHelperService;
    }

    @Override
    public void run() {
        timerHelperService.perform(quote, period);
    }
}
