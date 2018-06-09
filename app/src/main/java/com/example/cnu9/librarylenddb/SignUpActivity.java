package com.example.cnu9.librarylenddb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText editText_ID_SignUp;
    private EditText editText_Name_SignUp;
    private EditText editText_Birthday_SignUp;
    private EditText editText_PW_SignUp;
    private Button button_SignUp;
    private Button button_Cancel;
    private String ID;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editText_ID_SignUp = (EditText) findViewById(R.id.editText_ID_SignUp);
        editText_Name_SignUp = (EditText) findViewById(R.id.editText_Name_SignUp);
        editText_Birthday_SignUp = (EditText) findViewById(R.id.editText_Birthday_SignUp);
        editText_PW_SignUp = (EditText) findViewById(R.id.editText_PW_SignUp);
        button_SignUp = (Button) findViewById(R.id.button_SignUp);
        button_Cancel = (Button) findViewById(R.id.button_Cancel);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_SignUp:

                ID = editText_ID_SignUp.getText().toString();

                DatabaseReference conditionRef_User_ID = database.getReference("User").child(ID);
                DatabaseReference conditionRef_User_Name = database.getReference("User").child(ID).child("Name");
                DatabaseReference conditionRef_User_Birthday = database.getReference("User").child(ID).child("Birthday");
                DatabaseReference conditionRef_User_PW = database.getReference("User").child(ID).child("PW");

                conditionRef_User_Name.setValue(editText_Name_SignUp.getText().toString());
                conditionRef_User_Birthday.setValue(editText_Birthday_SignUp.getText().toString());
                conditionRef_User_PW.setValue(editText_PW_SignUp.getText().toString());
                finish();
                break;

            case R.id.button_Cancel:
                finish();
                break;
        }
    }
}
