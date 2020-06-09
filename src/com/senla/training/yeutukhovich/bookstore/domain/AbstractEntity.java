package com.senla.training.yeutukhovich.bookstore.domain;

public abstract class AbstractEntity implements Cloneable {

    protected final int id;

    protected AbstractEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // клонирование еще будет, пока что в нем нет необходимости
    // но я твою идею понял для использования в репозитории, с учетом наличия метода апдейт в репозитории
    // все супер, единственный момент - посмотри способы клонирования, мы будем проходить в том числе и этот,
    // но все же его рекомендуют не использовать (как вариант - копирующий конструктор)
    @Override
    public AbstractEntity clone() {
        try {
            return (AbstractEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }
}
