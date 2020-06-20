package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.cvs.BookCvsExchanger;

import java.util.List;

public class Test {
    public static void main(String[] args) {

//        IRepository<Book> bookRepository= BookRepository.getInstance();
//
//        EntityExchanger<Book> entityExchanger =new BookCvsExchanger();
//        entityExchanger.exportEntities(bookRepository.findAll(),"Books");


        List<Book> books=new BookCvsExchanger().importEntities("Books");

        System.out.println();


    }
}
