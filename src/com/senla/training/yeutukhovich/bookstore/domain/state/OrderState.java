package com.senla.training.yeutukhovich.bookstore.domain.state;

public enum OrderState {

    CREATED(1),
    CANCELED(2),
    COMPLETED(3);

    private int idNumber;

    OrderState(int idNumber) {
        this.idNumber = idNumber;
    }

    public int getIdNumber() {
        return idNumber;
    }
}
