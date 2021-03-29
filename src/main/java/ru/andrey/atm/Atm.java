package ru.andrey.atm;

import java.util.Map;

public interface Atm {

    void deposit(Map<Banknote, Integer> banknotes);

    Map<Banknote, Integer> withdraw(int amount) throws NotEnoughMoneyException;

    int getBalance();
}
