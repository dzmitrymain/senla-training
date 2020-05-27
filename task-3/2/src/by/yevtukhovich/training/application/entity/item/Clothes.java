package by.yevtukhovich.training.application.entity.item;

import by.yevtukhovich.training.application.entity.Item;

public class Clothes extends Item {

    private char size;

    public Clothes(String title, double weight, char size) {
        super(title, weight);
        this.size = size;
    }

    public char getSize() {
        return size;
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
        Clothes clothes = (Clothes) o;
        return size == clothes.size;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) size;
        return result;
    }

    @Override
    public String toString() {
        return "Clothes{" +
                "weight=" + getWeight() +
                '}';
    }
}
