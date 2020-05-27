package by.yevtukhovich.training.application.assembler.impl;

import by.yevtukhovich.training.application.assembler.IAssemblyLine;
import by.yevtukhovich.training.application.assembler.ILineStep;
import by.yevtukhovich.training.application.entity.IProduct;

import java.util.Objects;

public class AssemblyLine implements IAssemblyLine {

    private ILineStep firstLineStep;
    private ILineStep secondLineStep;
    private ILineStep thirdLineStep;

    public AssemblyLine(ILineStep firstLineStep, ILineStep secondLineStep, ILineStep thirdLineStep) {
        this.firstLineStep = firstLineStep;
        this.secondLineStep = secondLineStep;
        this.thirdLineStep = thirdLineStep;

        System.out.println("Assembly line has been created.");
    }

    @Override
    public IProduct assembleProduct(IProduct product) {

        if (product != null) {
            product.installFirstPart(firstLineStep.buildProductPart());
            product.installSecondPart(secondLineStep.buildProductPart());
            product.installThirdPart(thirdLineStep.buildProductPart());
        }
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssemblyLine that = (AssemblyLine) o;
        if (!Objects.equals(firstLineStep, that.firstLineStep)) {
            return false;
        }
        if (!Objects.equals(secondLineStep, that.secondLineStep)) {
            return false;
        }
        return Objects.equals(thirdLineStep, that.thirdLineStep);
    }

    @Override
    public int hashCode() {
        int result = firstLineStep != null ? firstLineStep.hashCode() : 0;
        result = 31 * result + (secondLineStep != null ? secondLineStep.hashCode() : 0);
        result = 31 * result + (thirdLineStep != null ? thirdLineStep.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AssemblyLine{" +
                "firstLineStep=" + firstLineStep +
                ", secondLineStep=" + secondLineStep +
                ", thirdLineStep=" + thirdLineStep +
                '}';
    }
}
