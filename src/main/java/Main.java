import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map.of(1000, 5,
                500, 2,
                100, 5,
                50, 100,
                30, 6
        );

        var nominals = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        nominals.put(1000, 5);
        nominals.put(500, 2);
        nominals.put(100, 5);
        nominals.put(50, 100);
        nominals.put(30, 6);


        getMoney(new BigDecimal("6750"), nominals);
    }

    static List<Nominal> getMoney(BigDecimal amount, TreeMap<Integer, Integer> nominals) {
        List<Nominal> nominalList = new ArrayList<>();
        var limits = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        limits.putAll(nominals);
        recur(amount, nominals, limits, nominalList);
        return nominalList;
    }

    static Integer recur(BigDecimal amount, TreeMap<Integer, Integer> nominals, TreeMap<Integer, Integer> limits, List<Nominal> nominalList) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) return 1;
        if (nominals.size() == 0) return null; // failure TODO change exception
        var nominal = nominals.entrySet().iterator().next();
        var count = Math.min(limits.get(nominal.getKey()), amount.divide(BigDecimal.valueOf(nominal.getKey().doubleValue()), 2, RoundingMode.HALF_DOWN).intValue());
        for (var i = count; i >= 0; i--) {
            nominals.remove(nominal.getKey());
            var result = recur(amount.subtract(new BigDecimal(i * nominal.getKey())), nominals, limits, nominalList);
            if (result != null) {
                if (i != 0) {
                    nominalList.add(0, new Nominal(nominal.getKey(), i));
                }
                return result;
            }
        }
        return null;
    }
}
