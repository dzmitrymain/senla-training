package by.yevtukhovich.training.application.entity.impl;

import by.yevtukhovich.training.application.entity.IProduct;
import by.yevtukhovich.training.application.entity.IProductPart;
import java.util.Objects;

public class Glasses implements IProduct {

    private Frame frame;
    private Lenses lenses;
    private Sidepieces sidepieces;

    public Frame getFrame() {
        return frame;
    }

    public Lenses getLenses() {
        return lenses;
    }

    public Sidepieces getSidepieces() {
        return sidepieces;
    }

    @Override
    public void installFirstPart(IProductPart productPart) {
        if (productPart != null && productPart.getClass() == Frame.class) {
            frame = (Frame) productPart;
            System.out.println("Frame has been added to the glasses.");
        }else{
            System.out.println("Frame adding failure. Product part is not a Frame.");
        }
    }

    @Override
    public void installSecondPart(IProductPart productPart) {
        if (productPart != null && productPart.getClass() == Lenses.class) {
            lenses = (Lenses) productPart;
            System.out.println("Lenses has been added to the glasses.");
        }else{
            System.out.println("Lenses adding failure. Product part is not a Lenses.");
        }
    }

    @Override
    public void installThirdPart(IProductPart productPart) {
        if (productPart != null && productPart.getClass() == Sidepieces.class) {
            sidepieces = (Sidepieces) productPart;
            System.out.println("Sidepieces has been added to the glasses.");
        }else{
            System.out.println("Sidepieces adding failure. Product part is not a Sidepieces.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Glasses glasses = (Glasses) o;
        if (!Objects.equals(frame, glasses.frame)) {
            return false;
        }
        if (!Objects.equals(lenses, glasses.lenses)) {
            return false;
        }
        return Objects.equals(sidepieces, glasses.sidepieces);
    }

    @Override
    public int hashCode() {
        int result = frame != null ? frame.hashCode() : 0;
        result = 31 * result + (lenses != null ? lenses.hashCode() : 0);
        result = 31 * result + (sidepieces != null ? sidepieces.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Glasses{" +
                "frame=" + frame +
                ", lenses=" + lenses +
                ", sidepieces=" + sidepieces +
                '}';
    }
}
