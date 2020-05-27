package by.yevtukhovich.training.application.assembler.impl;

import by.yevtukhovich.training.application.assembler.ILineStep;
import by.yevtukhovich.training.application.entity.IProductPart;
import by.yevtukhovich.training.application.entity.impl.Lenses;

public class LensesLineStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Lenses has been created.");
        return new Lenses();
    }

    @Override
    public String toString() {
        return "LensesLineStep";
    }
}
