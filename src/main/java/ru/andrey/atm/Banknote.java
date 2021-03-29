package ru.andrey.atm;

public class Banknote {

    private final Nominal nominal;

    public Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banknote banknote = (Banknote) o;

        return nominal == banknote.nominal;
    }

    @Override
    public int hashCode() {
        return nominal != null ? nominal.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "nominal=" + nominal +
                '}';
    }
}
