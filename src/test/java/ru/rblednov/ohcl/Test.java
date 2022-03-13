package ru.rblednov.ohcl;

import java.time.Instant;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(localDateTime.getMinute());
        System.out.println(Instant.now());
        System.out.println(System.currentTimeMillis());

    }
}
