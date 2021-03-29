package ru.andrey.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BankAtm implements Atm {

    private static Logger logger = LoggerFactory.getLogger(BankAtm.class);

    private final SortedMap<Nominal, Cell> cells;

    public BankAtm(Map<Nominal, Cell> cells) {
        var tempCells = new TreeMap<Nominal, Cell>(Comparator.reverseOrder());
        tempCells.putAll(cells);
        this.cells = tempCells;
    }

    @Override
    public void deposit(Map<Banknote, Integer> banknotes) {
        for (Map.Entry<Banknote, Integer> entry : banknotes.entrySet()) {
            Cell cell = cells.get(entry.getKey().getNominal());
            cell.setCount(cell.getCount() + entry.getValue());
            logger.info("Inserted {} banknotes of nominal {}", entry.getKey().getNominal().getValue(), entry.getValue());
        }
    }

    @Override
    public Map<Banknote, Integer> withdraw(int amount) throws NotEnoughMoneyException {
        if (getBalance() >= amount) {
            logger.info("Withdraw {}", amount);
            return getMoney(amount);
        } else {
            logger.info("Not enough banknotes to withdraw {}", amount);
            throw new NotEnoughMoneyException();
        }
    }

    @Override
    public int getBalance() {
        int balance = 0;
        for (Map.Entry<Nominal, Cell> entry : cells.entrySet()) {
            balance += entry.getValue().getNominal().getValue() * entry.getValue().getCount();
        }
        return balance;
    }


    public Map<Banknote, Integer> getMoney(int amount) {
        Map<Banknote, Integer> banknotesMap = new HashMap<>();
        var banknotes = new TreeMap<Nominal, Cell>(Collections.reverseOrder());
        banknotes.putAll(cells);
        getBanknotes(amount, banknotes, banknotesMap);
        if (banknotesMap.size() != 0) {
            extractFromStorage(banknotesMap);
            return banknotesMap;
        } else {
            throw new NoSuchElementException();
        }
    }

    public Integer getBanknotes(int amount, SortedMap<Nominal, Cell> cellsCopy, Map<Banknote, Integer> resultBanknotesMap) {
        if (amount == 0) {
            return 0;
        }
        if (cellsCopy.size() == 0) {
            return 1;
        }
        var nominal = cellsCopy.entrySet().iterator().next();
        var count = Math.min(cells.get(nominal.getKey()).getCount(), amount / nominal.getKey().getValue());
        for (var i = count; i >= 0; i--) {
            cellsCopy.remove(nominal.getKey());
            var result = getBanknotes(amount - i * nominal.getKey().getValue(), cellsCopy, resultBanknotesMap);
            if (result == 0) {
                if (i != 0) {
                    resultBanknotesMap.put(new Banknote(nominal.getValue().getNominal()), i);
                }
                return result;
            }
        }
        return 1;
    }

    public void extractFromStorage(Map<Banknote, Integer> banknotesMap) {
        for (Map.Entry<Banknote, Integer> entry : banknotesMap.entrySet()) {
            Cell current = cells.get(entry.getKey().getNominal());
            cells.remove(entry.getKey().getNominal());
            current.setCount(current.getCount() - entry.getValue());
            cells.put(entry.getKey().getNominal(), current);
        }

    }
}
