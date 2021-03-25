package card;

import owner.Owner;

import java.math.BigDecimal;

public class VerifiedCard extends Card {

    private Owner cardHolder;
    private String account;
    private BigDecimal balance;

    private String cardNumber;
    private int month;
    private int year;
    private String pin;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public VerifiedCard(Card card) {
        super(card.getBank(), card.getCardType());
    }

    public VerifiedCard(Owner cardHolder, String account, BigDecimal balance, String cardNumber, int month, int year, String pin) {
        super(null, null);
        this.cardHolder = cardHolder;
        this.account = account;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.month = month;
        this.year = year;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [%s: %s] | [%s: %s]",
                "Card number", cardNumber,
                "Account", account);
    }
}
