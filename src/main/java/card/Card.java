package card;

import bank.Bank;
import card.type.CardType;

public class Card {

    private Bank bank;
    private CardType cardType;

    public Card(Bank bank, CardType cardType) {
        this.bank = bank;
        this.cardType = cardType;
    }

    public Bank getBank() {
        return bank;
    }

    public CardType getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return String.format("[%s: %s]",
                "Bank name", bank.getName());
    }
}
