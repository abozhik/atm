package atm;

import bank.Bank;
import card.Card;
import card.VerifiedCard;
import cell.CurrencyCell;
import exception.NotEnoughMoneyException;
import exception.NotVerifiedCardException;
import nominal.CurrencyCode;
import nominal.Euro;
import nominal.RussianRuble;
import nominal.UnitedStatesDollar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class BankAtm extends Atm {

    private static final Logger logger = LoggerFactory.getLogger(BankAtm.class);

    private final CurrencyCell<RussianRuble> rubleCell;
    private final CurrencyCell<Euro> euroCell;
    private final CurrencyCell<UnitedStatesDollar> dollarCell;

    public BankAtm(Bank bank, CurrencyCell<RussianRuble> rubleCell, CurrencyCell<Euro> euroCell, CurrencyCell<UnitedStatesDollar> dollarCell) {
        super(bank);
        this.rubleCell = rubleCell;
        this.euroCell = euroCell;
        this.dollarCell = dollarCell;
    }

    @Override
    public void insertCard(Card card) {
        setCurrentCard(card);
        logger.info("Card entered {}", card);
    }

    @Override
    public void enterPin(String pin) throws NotVerifiedCardException {
        var account = getBank().getValidator().getAccount(getCurrentCard(), "1234");
        if (account.isPresent()) {
            VerifiedCard verifiedCard = new VerifiedCard(getCurrentCard());
            setCurrentCard(verifiedCard);
            logger.info("Card verified {}", verifiedCard);
        } else {
            logger.info("Wring pin code for card {}", getCurrentCard());
            throw new NotVerifiedCardException();
        }
    }

    @Override
    public void put(Map<Integer, Integer> banknotesMap) throws NotVerifiedCardException {
        if (getCurrentCard() instanceof VerifiedCard) {
            var verifiedCard = (VerifiedCard) getCurrentCard();
            var amount = countTotal(banknotesMap);
            verifiedCard.setBalance(verifiedCard.getBalance().add(amount));
            logger.info("Getting balance information for card {}", getCurrentCard());
        } else {
            logger.info("Trying to put money without entering pin code for card {}", getCurrentCard());
            throw new NotVerifiedCardException();
        }
    }

    @Override
    public Map<Integer, Integer> withdraw(CurrencyCode code, BigDecimal amount) throws NotEnoughMoneyException, NotVerifiedCardException {
        if (getCurrentCard() instanceof VerifiedCard) {
            var verifiedCard = (VerifiedCard) getCurrentCard();
            if (amount.add(amount.multiply(getBank().getPercentageCommissionForBank(getBank()))).compareTo(verifiedCard.getBalance()) > 0) {
                throw new NotEnoughMoneyException();
            }
            Map<Integer, Integer> banknotesMap;
            switch (code) {
                case EUR:
                    banknotesMap = euroCell.getMoney(amount);
                    break;
                case USD:
                    banknotesMap = dollarCell.getMoney(amount);
                    break;
                default:
                    banknotesMap = rubleCell.getMoney(amount);
            }
            verifiedCard.setBalance(verifiedCard.getBalance().subtract(amount));
            logger.info("Withdrawing money for card {}", getCurrentCard());
            return banknotesMap;
        } else {
            logger.info("Trying to withdraw money without entering pin code for card {}", getCurrentCard());
            throw new NotVerifiedCardException();
        }
    }

    @Override
    public BigDecimal getBalance() throws NotVerifiedCardException {
        if (getCurrentCard() instanceof VerifiedCard) {
            logger.info("Getting balance information for card {}", getCurrentCard());
            return ((VerifiedCard) getCurrentCard()).getBalance();
        } else {
            logger.info("Trying to get balance without entering pin code for card {}", getCurrentCard());
            throw new NotVerifiedCardException();
        }
    }

}
