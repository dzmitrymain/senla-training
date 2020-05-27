package by.yevtukhovich.training.application.entity;

import java.util.Arrays;

public class Warehouse {

    private static final double ARRAY_EXPANSION_CONSTANT = 1.5;

    private Item[] items;
    private double totalWeight;
    private double weightCapacity;

    public Warehouse(int initialArrayCapacity, double weightCapacity) {
        items = new Item[initialArrayCapacity];
        this.weightCapacity = weightCapacity;
        totalWeight = 0;
    }

    public boolean addItem(Item item) {

        if (item != null && (item.getWeight() + totalWeight) <= weightCapacity) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) {
                    addItemWithWeight(i, item);
                    return true;
                }
            }
            int index = expandArray();
            addItemWithWeight(index, item);
            return true;
        }
        return false;
    }

    public boolean removeItem(Item item) {

        if (item != null) {
            for (int i = 0; i < items.length; i++) {
                if (this.items[i] == item) {
                    this.items[i] = null;
                    totalWeight -= item.getWeight();
                    return true;
                }
            }
        }
        return false;
    }

    private int expandArray() {
        int oldItemsLength = items.length;

        Item[] newItems = new Item[(int) ((oldItemsLength * ARRAY_EXPANSION_CONSTANT)+1)];
        System.arraycopy(items, 0, newItems, 0, oldItemsLength);
        items = newItems;

        return oldItemsLength;
    }

    private void addItemWithWeight(int index, Item item) {
        items[index] = item;
        totalWeight += item.getWeight();
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
