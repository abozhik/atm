package nominal;

public class RussianRuble implements Currency {

    @Override
    public String getName() {
        return "Russian ruble";
    }

    @Override
    public String getCode() {
        return CurrencyCode.RUB.getCode();
    }

    @Override
    public String getSymbol() {
        return "₽";
    }

}
