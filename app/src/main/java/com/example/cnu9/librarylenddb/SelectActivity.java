package com.example.cnu9.librarylenddb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class SelectActivity extends AppCompatActivity {

    private Button button_LogOut;
    private Button button_Lend;
    private Button button_Return;

    SharedPreferences pref;

    private Intent intent_Lend;
    private Intent intent_Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        button_LogOut = (Button) findViewById(R.id.button_LogOut);
        button_Lend = (Button) findViewById(R.id.button_Lend);
        button_Return = (Button) findViewById(R.id.button_Return);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        Log.e("아이디 : ", pref.getString("ID","없음"));
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_LogOut:
                //모든 데이터 삭제, 로그아웃버튼 클릭시 실행
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;

            case R.id.button_Lend:
                intent_Lend = new Intent(this, LendActivity.class);
                startActivity(intent_Lend);
                break;

            case R.id.button_Return:
                intent_Return = new Intent(this, ReturnActivity.class);
                startActivity(intent_Return);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }
}
