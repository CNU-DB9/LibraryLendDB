package com.example.cnu9.librarylenddb;

//빌린 목록 조회 페이지

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReturnActivity extends AppCompatActivity {
    private final static int RETURNACTIVITY = 2;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mBook = mDatabase.child("Book");

    DatabaseReference mUser = mDatabase.child("User");

    SharedPreferences pref;

    EditText bookCodeInput;
    Button returnButton;

    ListView listView;
    BookAdapter adapter;
    ArrayList<Book> items = new ArrayList<Book>();

    String id;


    class BookAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Book item) {
            items.add(item);
        }

        public void removeItem(int position) {
            items.remove(position);
        } //index(position)값을 받아온 다음 해당 하는 값 list에서 삭제

        public void allRemove() {
            items.removeAll(items);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            BookView view = new BookView(getApplicationContext());

            Book item = items.get(position);
            view.setBookCode(item.getBookCode());
            view.setBookName(item.getBookName());
            view.setAuthor(item.getAuthor());
            view.setBookPublisher(item.getBookPublisher());

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.productListView);
        bookCodeInput = (EditText) findViewById(R.id.bookCodeInput);
        returnButton = (Button) findViewById(R.id.returnButton);

        adapter = new BookAdapter();


        id = pref.getString("ID","");


        if(mUser.child(id).child("LendBookCode") != null){ //ID부분에 MAIN에서 로그인한 아이디 받아와서 넣으면 됨

            mUser.child(id).child("LendBookCode").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String code = snapshot.getKey();
                        lendBookList(code);
                    }
                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            });


        }
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Return();
            }
        });


//        mBook.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                adapter.allRemove(); //리스트 내부를 모두 지웠다가 아래 for문으로 다시 생성
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String bookCode = snapshot.child("BookCode").getValue(String.class);
//                    String bookName = snapshot.child("BookName").getValue(String.class);
//                    String author = snapshot.child("Author").getValue(String.class);
//                    String bookPublisher = snapshot.child("BookPublisher").getValue(String.class);
//                    adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
//                    //listView.setAdapter(adapter); //리스트 뷰에 어댑터 객체 설정
//                }
//                    listView.setAdapter(adapter);
//
//                }
//
//                @Override
//                public void onCancelled (DatabaseError databaseError){
//
//                }
//            });

        //저장한 값을 이용하여 어댑터에 각 아이템 추가

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book item = (Book) adapter.getItem(position);
                String bookCode = item.getBookCode();
                String bookName = item.getBookName();
                String author = item.getAuthor();
                String bookPublisher = item.getBookPublisher();

                Intent intent = new Intent(ReturnActivity.this, DetailActivity.class);
                intent.putExtra("BookCode",bookCode);
                intent.putExtra("BookName",bookName);
                intent.putExtra("Author",author);
                intent.putExtra("BookPublisher",bookPublisher);
                intent.putExtra("PreviousActivity", RETURNACTIVITY);

                startActivity(intent);
            }
        });

    }

    public void lendBookList(String code) {
        mBook.child(code).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.allRemove(); //리스트 내부를 모두 지웠다가 아래 for문으로 다시 생성
                String bookCode = dataSnapshot.child("BookCode").getValue(String.class);
                String bookName = dataSnapshot.child("BookName").getValue(String.class);
                String author = dataSnapshot.child("Author").getValue(String.class);
                String bookPublisher = dataSnapshot.child("BookPublisher").getValue(String.class);
                adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
                listView.setAdapter(adapter); //리스트 뷰에 어댑터 객체 설정


//                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Return(){
        mUser.child(id).child("LendBookCode").child(bookCodeInput.getText().toString()).removeValue();
    }
}