package validator;

import card.Card;

import java.util.Optional;

public class ValidatorImpl implements Validator {

    @Override
    public Optional<String> getAccount(Card card, String pin) {
//      TODO implement it normally
        return Optional.ofNullable("1234");
    }

}
