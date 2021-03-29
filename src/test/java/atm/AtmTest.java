package atm;

import org.junit.jupiter.api.Test;
import ru.andrey.atm.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.andrey.atm.Nominal.*;

class AtmTest {

    private Atm atm;

    public AtmTest() {
        Map<Nominal, Cell> banknotesMap = new HashMap<>();
        banknotesMap.put(N_50, new Cell(N_50, 100));
        banknotesMap.put(N_100, new Cell(N_100, 100));
        banknotesMap.put(N_200, new Cell(N_200, 100));
        banknotesMap.put(N_500, new Cell(N_500, 100));
        banknotesMap.put(N_1000, new Cell(N_1000, 100));
        banknotesMap.put(N_2000, new Cell(N_2000, 100));
        banknotesMap.put(N_5000, new Cell(N_5000, 100));
        atm = new BankAtm(banknotesMap);
    }
    
    @Test
    void getBalanceTest() {
        //given
        Map<Nominal, Cell> banknotesMap = new HashMap<>();
        banknotesMap.put(N_50, new Cell(N_50, 100));
        banknotesMap.put(N_1000, new Cell(N_1000, 50));
        banknotesMap.put(N_5000, new Cell(N_5000, 30));
        atm = new BankAtm(banknotesMap);
        //when
        int balance = atm.getBalance();
        //then
        assertThat(balance).isEqualTo(205_000);
    }


    @Test
    void depositTest() {
        //given
        int balanceOld = atm.getBalance();
        Map<Banknote, Integer> banknotesMap = new HashMap<>();
        banknotesMap.put(new Banknote(N_500), 3);
        banknotesMap.put(new Banknote(N_1000), 2);
        //when
        atm.deposit(banknotesMap);
        //then
        int balanceNew = atm.getBalance();
        assertThat(balanceNew - (balanceOld)).isEqualTo(3_500);
    }

    @Test
    void withdrawTest() throws NotEnoughMoneyException {
        //given
        int amount = 6750;
        //when
        Map<Banknote, Integer> banknotesMap = atm.withdraw(amount);
        //then
        assertThat(banknotesMap)
                .containsEntry(new Banknote(N_5000), 1)
                .containsEntry(new Banknote(N_1000), 1)
                .containsEntry(new Banknote(N_500), 1)
                .containsEntry(new Banknote(N_200), 1)
                .containsEntry(new Banknote(N_50), 1);
    }


    @Test
    void withdrawTestNotEnoughMoney() {
        //given
        int amount = 5_000_000;
        //when
        try {
            atm.withdraw(amount);
        } catch (NotEnoughMoneyException e) {
            //then
            assertThat(e).isNotNull();
        }
    }

}
