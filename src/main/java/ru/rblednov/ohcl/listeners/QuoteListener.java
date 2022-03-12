package ru.rblednov.ohcl.listeners;

import ru.rblednov.ohcl.dto.Quote;

public interface QuoteListener {
    void onQuote(Quote quote);
}
