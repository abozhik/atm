package nominal;

public class UnitedStatesDollar implements Currency {

    @Override
    public String getName() {
        return "United States dollar";
    }

    @Override
    public String getCode() {
        return "USD";
    }

    @Override
    public String getSymbol() {
        return "$";
    }

}
