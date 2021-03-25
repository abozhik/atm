package validator;

import card.Card;

import java.util.Optional;

public interface Validator {

    Optional<String> getAccount(Card card, String pin);

}
