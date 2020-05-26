package by.yevtukhovich.training.application.entity;

import java.util.Objects;

public class Item {

    private String title;
    private double weight;

    public Item(String title, double weight) {
        this.title = title;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        if (Double.compare(item.weight, weight) != 0) return false;
        return Objects.equals(title, item.title);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + Double.hashCode(weight);
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", weight=" + weight +
                '}';
    }
}
