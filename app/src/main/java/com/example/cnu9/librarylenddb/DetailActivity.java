package com.example.cnu9.librarylenddb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    TextView text_bookName, text_author, text_bookPublisher, text_lendDate;
    Button returnButton, lendButton;
    SharedPreferences pref;
    String id;


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mDatabase.child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        Intent intent = getIntent();
        final String bookCode = intent.getStringExtra("BookCode");
        String bookName = intent.getStringExtra("BookName");
        String author = intent.getStringExtra("Author");
        String bookPublisher = intent.getStringExtra("BookPublisher");

        text_bookName = (TextView) findViewById(R.id.bookName);
        text_author = (TextView) findViewById(R.id.author);
        text_bookPublisher = (TextView) findViewById(R.id.bookPublisher);
        text_lendDate = (TextView) findViewById(R.id.lendDate);

        lendButton = (Button) findViewById(R.id.lendButton);
        returnButton = (Button) findViewById(R.id.returnButton);


        text_bookName.setText(bookName);
        text_author.setText(author);
        text_bookPublisher.setText(bookPublisher);

        //여기에 데이터베이스 접근하여 빌린 일자 받아와야함

        id = pref.getString("ID","");

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser.child(id).child("LendDate") != null){
                    mUser.child(id).child("LendBookCode").child(bookCode).removeValue();
                    mUser.child(id).child("LendDate").removeValue();
                    returnButton.setVisibility(View.INVISIBLE);
                    finish();
                }

            }
        });

        lendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mUser.child(id).child("LendBookCode").setValue()
            }
        });

    }
}
