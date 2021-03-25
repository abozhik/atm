package nominal;

public enum CurrencyCode {

    RUB ("rub"),
    USD("usd"),
    EUR("eur");

    private final String code;

    CurrencyCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
