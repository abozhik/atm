package bank;

import validator.Validator;

import java.math.BigDecimal;

public class BankNumberOne extends Bank {

    @Override
    public BigDecimal getPercentageCommissionForBank(Bank bank) {
        if (bank instanceof BankNumberOne) {
            return new BigDecimal(0);
        }
        return new BigDecimal(5);
    }

    public BankNumberOne(Validator validator, String name) {
        super(validator, name);
    }
}
