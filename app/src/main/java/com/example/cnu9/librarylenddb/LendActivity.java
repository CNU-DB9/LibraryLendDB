package com.example.cnu9.librarylenddb;

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

public class LendActivity extends AppCompatActivity {

    // 데이터베이스
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference book = database.child("Book");

    private ArrayList<Book> items = new ArrayList<Book>();  // Book 리스트

    private ListView listView;
    private BookAdapter adapter;
    private Button btnFind;
    private EditText inputFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);

        listView = findViewById(R.id.productListView);
        adapter = new BookAdapter();
        btnFind = findViewById(R.id.btn_find);
        inputFind = findViewById(R.id.input_find);

        btnFind.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = inputFind.getText().toString();

                if(bookName.isEmpty()){
                    toastMessage("책 이름을 입력해주세요!");
                }
                else {
                    searchBookList(bookName);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book item = (Book) adapter.getItem(position);

                Intent intent = new Intent(LendActivity.this, DetailActivity.class);

                intent.putExtra("BookCode", item.getBookCode());
                intent.putExtra("BookName",item.getBookName());
                intent.putExtra("Author",item.getAuthor());
                intent.putExtra("BookPublisher",item.getBookPublisher());

                startActivity(intent);
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

    // 책을 찾아 화면에 표시
    private void searchBookList(final String bookName){
        Log.d("SearchBookList", bookName);
        book.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.allRemove();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("SearchBookList For문", snapshot.child("BookName").getValue(String.class));
                    if(bookName.equals(snapshot.child("BookName").getValue(String.class))){
                        Log.d("SearchBookList For문", "if문 확인");
                        String bookCode = snapshot.child("BookCode").getValue(String.class);
                        String bookName = snapshot.child("BookName").getValue(String.class);
                        String author = snapshot.child("Author").getValue(String.class);
                        String bookPublisher = snapshot.child("BookPublisher").getValue(String.class);
                        adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
                    }
                    Log.d("SearchBookList For문", snapshot.getKey());
                }
                if(adapter.getCount() == 0){
                    toastMessage("책이 검색되지 않습니다!");
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TOAST 메시지 띄우기
    private void toastMessage(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
