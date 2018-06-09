package com.example.cnu9.librarylenddb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LendActivity extends AppCompatActivity {

    // 데이터베이스
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference book = database.child("Book");

    ArrayList<Book> items = new ArrayList<Book>();  // Book 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);
    }

    // 화면에 책들 표시
    private void refreshBookList(){

    }
}
