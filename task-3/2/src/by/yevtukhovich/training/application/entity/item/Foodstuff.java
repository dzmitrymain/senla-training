package by.yevtukhovich.training.application.entity.item;

import by.yevtukhovich.training.application.entity.Item;

public class Foodstuff extends Item {

    private int expiryDate;

    public Foodstuff(String title, double weight, int expiryDate) {
        super(title, weight);
        this.expiryDate = expiryDate;
    }

    public int getExpiryDate() {
        return expiryDate;
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
        Foodstuff that = (Foodstuff) o;
        return expiryDate == that.expiryDate;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + expiryDate;
        return result;
    }

    @Override
    public String toString() {
        return "Foodstuffs{" +
                "expiryDate=" + expiryDate +
                '}';
    }
}
