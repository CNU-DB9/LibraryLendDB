package com.example.cnu9.librarylenddb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LendActivity extends AppCompatActivity {

    // 데이터베이스
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference book = database.child("Book");

    ArrayList<Book> items = new ArrayList<Book>();  // Book 리스트

    ListView listView;
    BookAdapter adapter;
    Button btnFind;
    EditText inputFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);

        listView = (ListView) findViewById(R.id.productListView);
        adapter = new BookAdapter();
        btnFind = (Button) findViewById(R.id.btn_find);
        inputFind = (EditText) findViewById(R.id.input_find);

        btnFind.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.refreshBookList();
    }

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

    // 화면에 책들 표시
    private void refreshBookList() {
        book.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.allRemove(); //리스트 내부를 모두 지웠다가 아래 for문으로 다시 생성
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookCode = snapshot.child("BookCode").getValue(String.class);
                    String bookName = snapshot.child("BookName").getValue(String.class);
                    String author = snapshot.child("Author").getValue(String.class);
                    String bookPublisher = snapshot.child("BookPublisher").getValue(String.class);
                    adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void searchBookList(String bookName){
        book.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.allRemove();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("Test", "접속함.");
                    Log.d("Test", snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
