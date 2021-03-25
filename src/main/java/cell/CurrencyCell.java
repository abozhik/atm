package cell;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CurrencyCell<C> implements Cell {

    private C currency;
    private SortedMap<Integer, Integer> storage;

    public CurrencyCell(C currency, SortedMap<Integer, Integer> storage) {
        this.currency = currency;
        this.storage = storage;
    }

    public Map<Integer, Integer> getMoney(BigDecimal amount) {
        Map<Integer, Integer> banknotesMap = new HashMap<>();
        var banknotes = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        banknotes.putAll(storage);
        getBanknotes(amount, banknotes, banknotesMap);
        if (banknotesMap.size() != 0) {
            extractFromStorage(banknotesMap);
            return banknotesMap;
        } else {
            throw new NoSuchElementException();
        }
    }

    public Integer getBanknotes(BigDecimal amount, SortedMap<Integer, Integer> nominals, Map<Integer, Integer> banknotesMap) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) return 0;
        if (nominals.size() == 0) {
            return 1;
        }
        var nominal = nominals.entrySet().iterator().next();
        var count = Math.min(storage.get(nominal.getKey()), amount.divide(BigDecimal.valueOf(nominal.getKey().doubleValue()), 2, RoundingMode.HALF_DOWN).intValue());
        for (var i = count; i >= 0; i--) {
            nominals.remove(nominal.getKey());
            var result = getBanknotes(amount.subtract(new BigDecimal(i * nominal.getKey())), nominals, banknotesMap);
            if (result == 0) {
                if (i != 0) {
                    banknotesMap.put(nominal.getKey(), i);
                }
                return result;
            }
        }
        return 1;
    }

    public void extractFromStorage(Map<Integer, Integer> banknotesMap) {
        for (Map.Entry<Integer, Integer> entry : banknotesMap.entrySet()) {
            Integer current = storage.get(entry.getKey());
            storage.remove(entry.getKey());
            storage.put(entry.getKey(), current - entry.getValue());
        }

    }
}
