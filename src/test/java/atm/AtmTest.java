package atm;

import bank.Bank;
import bank.BankNumberOne;
import card.Card;
import card.VerifiedCard;
import card.type.DebitCard;
import cell.CurrencyCell;
import exception.NotEnoughMoneyException;
import exception.NotVerifiedCardException;
import nominal.CurrencyCode;
import nominal.Euro;
import nominal.RussianRuble;
import nominal.UnitedStatesDollar;
import org.junit.jupiter.api.Test;
import validator.ValidatorImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class AtmTest {

    private final Atm atm;
    private final Bank bank;

    public AtmTest() {
        SortedMap<Integer, Integer> banknotesMap = new TreeMap<>();
        banknotesMap.put(50, 100);
        banknotesMap.put(100, 100);
        banknotesMap.put(200, 100);
        banknotesMap.put(500, 100);
        banknotesMap.put(1000, 100);
        banknotesMap.put(2000, 100);
        banknotesMap.put(5000, 100);
        var rubleCurrencyCell = new CurrencyCell<>(new RussianRuble(), banknotesMap);
        var dollarCurrencyCell = new CurrencyCell<>(new UnitedStatesDollar(), banknotesMap);
        var euroCurrencyCell = new CurrencyCell<>(new Euro(), banknotesMap);
        bank = new BankNumberOne(new ValidatorImpl(), "one");
        atm = new BankAtm(bank, rubleCurrencyCell, euroCurrencyCell, dollarCurrencyCell);
    }

    @Test
    void insertCardTest() {
        //given
        Card cardOld = atm.getCurrentCard();
        Card card = new Card(bank, new DebitCard());
        //when
        atm.insertCard(card);
        //then
        assertThat(cardOld).isNull();
        assertThat(atm.getCurrentCard()).isNotNull();
    }

    @Test
    void enterPinTest() throws NotVerifiedCardException {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        //when
        atm.enterPin("1234");
        //then
        assertThat(atm.getCurrentCard()).isInstanceOf(VerifiedCard.class);
    }

    @Test
    void getBalanceTest() throws NotVerifiedCardException {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        atm.enterPin("1234");
        //when
        BigDecimal balance = atm.getBalance();
        //then
        assertThat(balance).isEqualTo(((VerifiedCard) atm.getCurrentCard()).getBalance());
    }


    @Test
    void getBalanceTestWithoutPinCode() {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        //when
        try {
            atm.getBalance();
        } catch (NotVerifiedCardException e) {
            //then
            assertThat(e).isNotNull();
        }
    }

    @Test
    void countTotalTest() {
        //given
        Map<Integer, Integer> banknotesMap = new HashMap<>();
        banknotesMap.put(500, 3);
        banknotesMap.put(1000, 2);
        banknotesMap.put(5000, 4);
        //when
        BigDecimal total = atm.countTotal(banknotesMap);
        //then
        assertThat(total).isEqualTo(new BigDecimal(23_500).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void putTest() throws NotVerifiedCardException {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        atm.enterPin("1234");
        ((VerifiedCard) atm.getCurrentCard()).setBalance(new BigDecimal(10_000));

        BigDecimal balanceOld = atm.getBalance();
        Map<Integer, Integer> banknotesMap = new HashMap<>();
        banknotesMap.put(500, 3);
        banknotesMap.put(1000, 2);
        //when
        atm.put(banknotesMap);
        //then
        BigDecimal balanceNew = atm.getBalance();
        assertThat(balanceNew.subtract(balanceOld)).isEqualTo(new BigDecimal(3_500).setScale(2, RoundingMode.CEILING));
    }

    @Test
    void withdrawTest() throws NotVerifiedCardException, NotEnoughMoneyException {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        atm.enterPin("1234");
        ((VerifiedCard) atm.getCurrentCard()).setBalance(new BigDecimal(10_000));
        //given
        BigDecimal amount = new BigDecimal(6750);
        //when
        Map<Integer, Integer> banknotesMap = atm.withdraw(CurrencyCode.RUB, amount);
        //then
        assertThat(banknotesMap)
                .containsEntry(5000, 1)
                .containsEntry(1000, 1)
                .containsEntry(500, 1)
                .containsEntry(200, 1)
                .containsEntry(50, 1);
    }


    @Test
    void withdrawTestNotEnoughMoney() throws NotVerifiedCardException {
        //given
        Card card = new Card(bank, new DebitCard());
        atm.insertCard(card);
        atm.enterPin("1234");
        ((VerifiedCard) atm.getCurrentCard()).setBalance(new BigDecimal(10_000));
        //given
        BigDecimal amount = new BigDecimal("5000000");
        //when
        try {
            atm.withdraw(CurrencyCode.RUB, amount);
        } catch (NotEnoughMoneyException e) {
            //then
            assertThat(e).isNotNull();
        }
    }

}
