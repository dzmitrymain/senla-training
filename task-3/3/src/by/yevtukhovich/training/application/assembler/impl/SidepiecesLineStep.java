package by.yevtukhovich.training.application.assembler.impl;

import by.yevtukhovich.training.application.assembler.ILineStep;
import by.yevtukhovich.training.application.entity.IProductPart;
import by.yevtukhovich.training.application.entity.impl.Sidepieces;

public class SidepiecesLineStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Sidepieces has been created.");
        return new Sidepieces();
    }

    @Override
    public String toString() {
        return "SidepiecesLineStep";
    }
}
