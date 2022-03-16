package ru.rblednov.ohcl.services.mutex;

import ru.rblednov.ohcl.dto.Quote;

public interface MutexService {
    Object getMutex(Quote quote);
}
