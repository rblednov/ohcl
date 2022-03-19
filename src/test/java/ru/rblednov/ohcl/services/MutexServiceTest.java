package ru.rblednov.ohcl.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rblednov.ohcl.services.current.CurrentOhlcHolderMutexServiceImpl;
import ru.rblednov.ohcl.services.mutex.MutexService;

@ExtendWith(SpringExtension.class)
@Import(CurrentOhlcHolderMutexServiceImpl.class)
public class MutexServiceTest {
    @Autowired
    MutexService mutexService;

    @Test
    public void test(){

    }
}
