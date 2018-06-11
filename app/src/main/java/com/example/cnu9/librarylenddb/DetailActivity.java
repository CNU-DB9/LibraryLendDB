package com.example.cnu9.librarylenddb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView text_bookName, text_author, text_bookPublisher, text_lendDate, text_returnDate;
    LinearLayout lendDate_Group, returnDate_Group, isLend;
    Button returnButton, lendButton;
    SharedPreferences pref;
    String id;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mBook = mDatabase.child("Book");
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
        int previousActivity = intent.getIntExtra("PreviousActivity", 1);

        text_bookName = (TextView) findViewById(R.id.bookName);
        text_author = (TextView) findViewById(R.id.author);
        text_bookPublisher = (TextView) findViewById(R.id.bookPublisher);
        text_lendDate = (TextView) findViewById(R.id.lendDate);
        text_returnDate = findViewById(R.id.returnDate);
        lendDate_Group = findViewById(R.id.lendGroup);
        returnDate_Group = findViewById(R.id.returnGroup);
        isLend = findViewById(R.id.isLend);

        lendButton = (Button) findViewById(R.id.lendButton);
        returnButton = (Button) findViewById(R.id.returnButton);

        //여기에 데이터베이스 접근하여 빌린 일자 받아와야함
        id = pref.getString("ID","");

        text_bookName.setText(bookName);
        text_author.setText(author);
        text_bookPublisher.setText(bookPublisher);

        //여기에 데이터베이스 접근하여 빌린 일자 받아와야함
        id = pref.getString("ID","");

        // 대출일 때,
        if(previousActivity == 1){
            returnButton.setVisibility(View.INVISIBLE);
            lendDate_Group.setVisibility(View.INVISIBLE);
            returnDate_Group.setVisibility(View.INVISIBLE);
            isLend.setVisibility(View.INVISIBLE);
            mBook.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean stock = dataSnapshot.child(bookCode).child("stock").getValue(Boolean.class);
                    if(!stock){
                        lendButton.setVisibility(View.INVISIBLE);
                        isLend.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(previousActivity == 2){
            lendButton.setVisibility(View.INVISIBLE);
            lendDate_Group.setVisibility((View.VISIBLE));
            isLend.setVisibility(View.INVISIBLE);
            mUser.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd");
                    String lendDate = dataSnapshot.child("LendBookCode").child(bookCode).getValue(String.class);
                    if(lendDate != null) {
                        ParsePosition pp = new ParsePosition(0);
                        Date date = dayTime.parse(lendDate, pp);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.DATE, 7);

                        text_lendDate.setText(lendDate);
                        text_returnDate.setText(dayTime.format(cal.getTime()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            if(mBook.child(bookCode).child("stock").getKey() == "false"){
//                returnButton.setVisibility(View.INVISIBLE);
//            }
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser.child(id).child("LendBookCode") != null){
                    mUser.child(id).child("LendBookCode").child(bookCode).removeValue();
                    mBook.child(bookCode).child("stock").setValue(true);
                    returnButton.setVisibility(View.INVISIBLE);
                    toastMessage("반납하셨습니다.");
                    finish();
                }

            }
        });

        lendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd");
                String strDayTime = dayTime.format(new Date());

                mUser.child(id).child("LendBookCode").child(bookCode).setValue(strDayTime);
                mBook.child(bookCode).child("stock").setValue(false);
                lendButton.setVisibility(View.INVISIBLE);
                toastMessage("대출하였습니다.");
                finish();
            }
        });

    }

    // TOAST 메시지 띄우기
    private void toastMessage(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
