package nominal;

public class Euro implements Currency {

    @Override
    public String getName() {
        return "Euro";
    }

    @Override
    public String getCode() {
        return "EUR";
    }

    @Override
    public String getSymbol() {
        return "€";
    }
}
