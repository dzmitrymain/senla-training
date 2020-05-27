package by.yevtukhovich.training.application.assembler.impl;

import by.yevtukhovich.training.application.assembler.ILineStep;
import by.yevtukhovich.training.application.entity.IProductPart;
import by.yevtukhovich.training.application.entity.impl.Frame;


public class FrameLineStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("Frame has been created.");
        return new Frame();
    }

    @Override
    public String toString() {
        return "FrameLineStep";
    }
}

