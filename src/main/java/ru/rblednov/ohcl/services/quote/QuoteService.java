package ru.rblednov.ohcl.services.quote;

import ru.rblednov.ohcl.dto.Quote;

public interface QuoteService {
    void processQuote(Quote quote);
}
