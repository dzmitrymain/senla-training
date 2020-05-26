package by.yevtukhovich.training.application.entity.item.households;

import by.yevtukhovich.training.application.entity.item.Household;

import java.util.Objects;

public class Furniture extends Household {

    private String material;

    public Furniture(String title, double weight, int fragile, String material) {
        super(title, weight, fragile);
        this.material = material;
    }

    public String getMaterial() {
        return material;
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
        Furniture furniture = (Furniture) o;
        return Objects.equals(material, furniture.material);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (material != null ? material.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "material='" + material + '\'' +
                '}';
    }
}
