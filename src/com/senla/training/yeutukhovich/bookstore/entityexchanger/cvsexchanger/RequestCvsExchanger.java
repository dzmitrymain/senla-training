package com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsexchanger;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.EntityExchanger;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.List;

public class RequestCvsExchanger extends AbstractCvsExchanger implements EntityExchanger<Request> {

    private static EntityExchanger<Request> instance;

    private RequestCvsExchanger() {

    }

    public static EntityExchanger<Request> getInstance() {
        if (instance == null) {
            instance = new RequestCvsExchanger();
        }
        return instance;
    }


    @Override
    public void exportEntities(List<Request> entities, String fileName) {
        List<String> requestStrings = entityCvsConverter.convertRequests(entities);
        if (!requestStrings.isEmpty()) {
            FileDataWriter.writeData(buildPath(fileName), requestStrings);
        }
    }

    @Override
    public int importEntities(String fileName) {
        int importedRequestsNumber = 0;
        List<String> requestsStrings = FileDataReader.readData(buildPath(fileName));

        List<Request> repoRequests = requestRepository.findAll();
        List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);

        for (Request importedRequest : importedRequests) {
            if (repoRequests.contains(importedRequest)) {
                requestRepository.update(importedRequest);
            } else {
                requestRepository.add(importedRequest);
            }
            importedRequestsNumber++;
        }
        return importedRequestsNumber;
    }
}
