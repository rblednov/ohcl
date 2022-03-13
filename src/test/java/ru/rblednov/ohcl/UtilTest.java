package ru.rblednov.ohcl;

import org.junit.jupiter.api.Test;
import ru.rblednov.ohcl.dto.Ohlc;
import ru.rblednov.ohcl.dto.OhlcPeriod;

public class UtilTest {
    @Test
    public void test() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime);
//        System.out.println(localDateTime.getMinute());
//        System.out.println(Instant.now());
//        System.out.println(System.currentTimeMillis());
//        Map<String, List<Integer>> map = new HashMap<>();
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        map.put("qwe", list);
//        map.get("qwe").add(2);
//        System.out.println(map);
        Ohlc ohlc1 = new Ohlc(120,150,110,130, OhlcPeriod.M1, System.currentTimeMillis());
        Ohlc ohlc2 = ohlc1.clone();
        ohlc2.setHighPrice(170);
        System.out.println(ohlc1);
        System.out.println(ohlc2);
        System.out.println(ohlc1);

    }
}
