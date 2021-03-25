package atm;

import bank.Bank;
import card.Card;
import exception.NotEnoughMoneyException;
import exception.NotVerifiedCardException;
import nominal.CurrencyCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public abstract class Atm {

    private Bank bank;
    private Card currentCard;

    Atm(Bank bank) {
        this.bank = bank;
    }

    abstract void insertCard(Card card);

    abstract void enterPin(String pin) throws NotVerifiedCardException;

    abstract void put(Map<Integer, Integer> banknotesMap) throws NotVerifiedCardException;

    abstract Map<Integer, Integer> withdraw(CurrencyCode code, BigDecimal amount) throws NotEnoughMoneyException, NotVerifiedCardException;

    abstract BigDecimal getBalance() throws NotVerifiedCardException;

    public BigDecimal countTotal(Map<Integer, Integer> banknotesMap) {
        int total = 0;
        for (var entry : banknotesMap.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return new BigDecimal(total).setScale(2, RoundingMode.CEILING);
    }


    public Bank getBank() {
        return bank;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }
}
