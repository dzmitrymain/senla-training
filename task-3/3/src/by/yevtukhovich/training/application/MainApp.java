package by.yevtukhovich.training.application;

import by.yevtukhovich.training.application.assembler.IAssemblyLine;
import by.yevtukhovich.training.application.assembler.impl.AssemblyLine;
import by.yevtukhovich.training.application.assembler.impl.FrameLineStep;
import by.yevtukhovich.training.application.assembler.impl.LensesLineStep;
import by.yevtukhovich.training.application.assembler.impl.SidepiecesLineStep;
import by.yevtukhovich.training.application.entity.IProduct;
import by.yevtukhovich.training.application.entity.impl.Glasses;

public class MainApp {
    
    public static void main(String[] args) {

        IAssemblyLine assemblyLine=new AssemblyLine(new FrameLineStep(),new LensesLineStep(),new SidepiecesLineStep());
        IProduct assembledProduct= assemblyLine.assembleProduct(new Glasses());

        System.out.println("Assembled product: "+assembledProduct);
    }
}
