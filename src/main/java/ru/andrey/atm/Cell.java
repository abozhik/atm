package ru.andrey.atm;

public class Cell {

    private final Nominal nominal;
    private int count;

    public Cell(Nominal nominal, int count) {
        this.nominal = nominal;
        this.count = count;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return nominal == cell.nominal;
    }

    @Override
    public int hashCode() {
        return nominal != null ? nominal.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "nominal=" + nominal +
                '}';
    }

}
