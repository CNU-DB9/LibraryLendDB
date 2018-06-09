package com.example.cnu9.librarylenddb;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    private String ID;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        //Toast.makeText(getApplicationContext(),pref.getString("ID",""), Toast.LENGTH_LONG).show();

        /**
        데이터 불러오기
        ID = pref.getString("ID","");
        */
    }

    public void onClick(View view) {

    }
}
