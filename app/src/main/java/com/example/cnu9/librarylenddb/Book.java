package com.example.cnu9.librarylenddb;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Book implements Serializable {

    private String bookCode;//책 고유 코드
    private String bookName; //책이름
    private String author; //작가
    private String bookPublisher; //출판사

    public Map<String, Boolean> updateProduct = new HashMap<>();

    public Book(){

    }

    public Book(String bookCode) {
        this.bookCode = bookCode;
    }

    public Book(String bookName, String author, String bookPublisher){
        this.bookName = bookName;
        this.author = author;
        this.bookPublisher = bookPublisher;
    }

    public Book(String bookCode, String bookName, String author, String bookPublisher){
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.author = author;
        this.bookPublisher = bookPublisher;
    }

//    public Book(String bookCode, String bookName, String author, String bookPublisher, int x){
//        this.bookCode = bookCode;
//        this.bookName = bookName;
//        this.author = author;
//        this.bookPublisher = bookPublisher;
//        this.x = x;
//    }


    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("BookCode", bookCode);
        result.put("BookName", bookName);
        result.put("Author", author);
        result.put("BookPublisher", bookPublisher);
        return result;
    }




}
