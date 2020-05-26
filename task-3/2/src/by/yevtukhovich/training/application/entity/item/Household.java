package by.yevtukhovich.training.application.entity.item;

import by.yevtukhovich.training.application.entity.Item;

public class Household extends Item {

private int fragile;

    public Household(String title, double weight, int fragile) {
        super(title, weight);
        this.fragile = fragile;
    }

    public int getFragile() {
        return fragile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Household that = (Household) o;
        return fragile == that.fragile;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + fragile;
        return result;
    }

    @Override
    public String toString() {
        return "Households{" +
                "fragile=" + fragile +
                '}';
    }
}
