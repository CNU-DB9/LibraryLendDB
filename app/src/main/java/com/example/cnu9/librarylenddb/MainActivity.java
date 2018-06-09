package com.example.cnu9.librarylenddb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button button_LogIn;
    private Button button_SignUp;

    private Intent intent_LogIn;
    private Intent intent_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_LogIn = (Button) findViewById(R.id.button_LogIn);
        button_SignUp = (Button) findViewById(R.id.button_SignUp);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_LogIn:
                intent_LogIn = new Intent(this, LogInActivity.class);
                startActivity(intent_LogIn);
                break;

            case R.id.button_SignUp:
                intent_SignUp = new Intent(this, SignUpActivity.class);
                startActivity(intent_SignUp);
                break;
        }
    }
}
