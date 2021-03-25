package bank;

import validator.Validator;

import java.math.BigDecimal;

public abstract class Bank {

    private final Validator validator;
    private final String name;

    public String getName() {
        return name;
    }

    public abstract BigDecimal getPercentageCommissionForBank(Bank bank);

    public Bank(Validator validator, String name) {
        this.validator = validator;
        this.name = name;
    }

    public Validator getValidator() {
        return validator;
    }

}
