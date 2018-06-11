package com.example.cnu9.librarylenddb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

public class LogInActivity extends Activity {

    private EditText editText_ID;
    private EditText editText_PW;
    private String ID;
    private String PW;

    private SharedPreferences pref;

    private Intent intent_Select;
    private Intent intent_Admin;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference conditionRef_User = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("로그인");
        setContentView(R.layout.activity_log_in);

        editText_ID = (EditText) findViewById(R.id.editText_ID);
        editText_PW = (EditText) findViewById(R.id.editText_PW);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        editText_ID.getText().clear();
        editText_PW.getText().clear();

        Log.e("아이디 : ", pref.getString("ID","없음"));

    }

    public void onClick(View view) {
        ID = editText_ID.getText().toString();
        PW = editText_PW.getText().toString();

        intent_Admin = new Intent(this, AdminActivity.class);
        intent_Select = new Intent(this, SelectActivity.class);

        conditionRef_User.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //Log.e("key : ",snapshot.getKey()+"    value : "+snapshot.getValue());

                    if(ID.equals(snapshot.getKey())) {
                        if (ID.equals("admin")) {
                            if(PW.equals(snapshot.child("PW").getValue(String.class))) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("ID", ID);
                                editor.commit();
                                startActivity(intent_Admin);
                                finish();
                            }
                        } else {
                            if(PW.equals(snapshot.child("PW").getValue(String.class))) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("ID", ID);
                                editor.commit();
                                startActivity(intent_Select);
                                finish();
                            }
                        }
                    }
                }
                //Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //^^바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
