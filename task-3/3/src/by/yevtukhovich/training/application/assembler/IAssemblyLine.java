package by.yevtukhovich.training.application.assembler;

import by.yevtukhovich.training.application.entity.IProduct;

public interface IAssemblyLine {

    IProduct assembleProduct(IProduct product);
}
