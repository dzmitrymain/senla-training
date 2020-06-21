package com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsexchanger;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.AbstractCvsExchanger;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.EntityExchanger;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.List;

public class OrderCvsExchanger extends AbstractCvsExchanger implements EntityExchanger<Order> {

    private static EntityExchanger<Order> instance;

    private OrderCvsExchanger() {

    }

    public static EntityExchanger<Order> getInstance() {
        if (instance == null) {
            instance = new OrderCvsExchanger();
        }
        return instance;
    }


    @Override
    public void exportEntities(List<Order> entities, String fileName) {
        List<String> orderStrings = entityCvsConverter.convertOrders(entities);
        if (!orderStrings.isEmpty()) {
            FileDataWriter.writeData(buildPath(fileName), orderStrings);
        }
    }

    @Override
    public int importEntities(String fileName) {
        int importedOrdersNumber = 0;
        List<String> orderStrings = FileDataReader.readData(buildPath(fileName));

        List<Order> repoOrders = orderRepository.findAll();
        List<Order> importedOrders = entityCvsConverter.parseOrders(orderStrings);

        for (Order importedOrder : importedOrders) {
            if (repoOrders.contains(importedOrder)) {
                orderRepository.update(importedOrder);
            } else {
                orderRepository.add(importedOrder);
            }
            importedOrdersNumber++;
        }
        return importedOrdersNumber;
    }
}
