package by.yevtukhovich.training.application;

import by.yevtukhovich.training.application.entity.*;
import by.yevtukhovich.training.application.entity.item.*;
import by.yevtukhovich.training.application.entity.item.household.*;

public class MainApp {

    private static int WAREHOUSE_CAPACITY=5;

    public static void main(String[] args) {

        Warehouse warehouse=new Warehouse(WAREHOUSE_CAPACITY);

        Item cookie=new Foodstuff("Awesome cookie",1.5,3);
        Item jeans=new Clothes("Men's jeans",9,'M');
        Item laptop=new Electronics("Acer", 8.3,10,200);
        Item chair=new Furniture("Comfortable",15,4,"wood");
        Item sofa=new Furniture("Corner sofa",152,3,"velour");
        Item fridge=new Electronics("White fridge", 145,6,350);

        warehouse.addItem(cookie);
        warehouse.addItem(jeans);
        warehouse.addItem(laptop);
        warehouse.addItem(chair);
        warehouse.addItem(sofa);
        warehouse.addItem(fridge);

        System.out.println("Warehouse items total weight: "+warehouse.getTotalWeight());
    }
}
