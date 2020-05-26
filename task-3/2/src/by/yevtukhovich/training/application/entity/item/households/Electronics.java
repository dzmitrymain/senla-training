package by.yevtukhovich.training.application.entity.item.households;

import by.yevtukhovich.training.application.entity.item.Household;

public class Electronics extends Household {

    private int powerConsumption;

    public Electronics(String title, double weight, int fragile, int powerConsumption) {
        super(title, weight, fragile);
        this.powerConsumption = powerConsumption;
    }

    public int getPowerConsumption() {
        return powerConsumption;
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
        Electronics that = (Electronics) o;
        return powerConsumption == that.powerConsumption;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + powerConsumption;
        return result;
    }

    @Override
    public String toString() {
        return "Electronics{" +
                "powerConsumption=" + powerConsumption +
                '}';
    }
}
