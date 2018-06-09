package com.example.cnu9.librarylenddb;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookView extends LinearLayout {

    TextView bookCode;//책 고유 코드
    TextView bookName; //책이름
    TextView author; //작가
    TextView bookPublisher; //출판사

    public BookView(Context context) {
        super(context);
        init(context);
    }

    public BookView(Context context, AttributeSet atts) {
        super(context, atts);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.book, this, true);

        bookCode = (TextView) findViewById(R.id.bookCode);
        bookName = (TextView) findViewById(R.id.bookName);
        author = (TextView) findViewById(R.id.author);
        bookPublisher = (TextView) findViewById(R.id.bookPublisher);

    }

    public void setBookCode(String code) {
        bookCode.setText(code);
    }

    public void setBookName(String name) {
        bookName.setText(name);
    }

    public void setAuthor(String authorName) {
        author.setText(authorName);
    }

    public void setBookPublisher(String publisher) {
        bookPublisher.setText(publisher);
    }



}