package by.yevtukhovich.training.application.entity;

import java.util.Arrays;

public class Warehouse {

    private Item[] items;
    private double totalWeight;

    public Warehouse(int warehouseCapacity) {
        items = new Item[warehouseCapacity];
        totalWeight = 0;
    }

    public boolean addItem(Item item) {

        for (int i = 0; i < items.length; i++) {
            if (this.items[i] == null) {
                this.items[i] = item;
                totalWeight += item.getWeight();
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(Item item) {

        for (int i = 0; i < items.length; i++) {
            if (this.items[i] == item) {
                this.items[i] = null;
                totalWeight -= item.getWeight();
                return true;
            }
        }
        return false;
    }


    public boolean hasEmptySpace() {

        for (Item item : items) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    public Item[] getItems() {
        return items;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Warehouse warehouse = (Warehouse) o;
        if (Double.compare(warehouse.totalWeight, totalWeight) != 0) {
            return false;
        }
        return Arrays.equals(items, warehouse.items);
    }

    @Override
    public int hashCode() {
        int result;
        result = Arrays.hashCode(items);
        result = 31 * result + Double.hashCode(totalWeight);
        return result;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "items=" + Arrays.toString(items) +
                ", totalWeight=" + totalWeight +
                '}';
    }
}
