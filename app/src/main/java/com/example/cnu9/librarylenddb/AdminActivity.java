package com.example.cnu9.librarylenddb;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private String id;

    private SharedPreferences pref;

    ListView listView;
    Button addBookbtn,searchBookbtn, logout;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mBook = mDatabase.child("Book");

    BookAdapter adapter;
    ArrayList<Book> items = new ArrayList<Book>();

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
        setContentView(R.layout.activity_admin);

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        addBookbtn = (Button) findViewById(R.id.addBook);
        searchBookbtn = (Button) findViewById(R.id.searchBook);
        logout = findViewById(R.id.logout);
        listView = (ListView) findViewById(R.id.productListView);
        adapter = new BookAdapter();

        //Toast.makeText(getApplicationContext(),pref.getString("ID",""), Toast.LENGTH_LONG).show();

        /**
        데이터 불러오기
        ID = pref.getString("ID","");
        */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        mBook.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.allRemove(); //리스트 내부를 모두 지웠다가 아래 for문으로 다시 생성
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookCode = snapshot.child("BookCode").getValue(String.class);
                    String bookName = snapshot.child("BookName").getValue(String.class);
                    String author = snapshot.child("Author").getValue(String.class);
                    String bookPublisher = snapshot.child("BookPublisher").getValue(String.class);
                    adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
                    //listView.setAdapter(adapter); //리스트 뷰에 어댑터 객체 설정
                }
                    listView.setAdapter(adapter);

                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            });


        addBookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });


        searchBookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBook();
            }
        });
    }

    public void addBook(){
        final Dialog dialog = new Dialog(AdminActivity.this);
        dialog.setTitle("원하시는 메뉴를 선택해 주세요");
        dialog.setContentView(R.layout.add_book);

        dialog.show();
        dialog.setOwnerActivity(AdminActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        final EditText bookCode = (EditText) dialog.findViewById(R.id.bookCode);
        final EditText bookName = (EditText) dialog.findViewById(R.id.bookName);
        final EditText author = (EditText) dialog.findViewById(R.id.author);
        final EditText bookPublisher = (EditText) dialog.findViewById(R.id.bookPublisher);


        Button addBook = (Button) dialog.findViewById(R.id.addBook);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Product createList = new Product(productBacode.getText().toString());
                //Map<String,Object> createListMap = createList.toMap();
                Book addList = new Book(bookCode.getText().toString(), bookName.getText().toString(), author.getText().toString(), bookPublisher.getText().toString());
                Map<String, Object> addListMap = addList.toMap();
                //mProduct.updateChildren(createListMap);
                mBook.child(bookCode.getText().toString()).updateChildren(addListMap);
                dialog.hide();
            }
        });
    }

    public void searchBook() {
        final Dialog dialog = new Dialog(AdminActivity.this);
        dialog.setTitle("찾고자 하는 상품의 이름을 입력해주세요");
        dialog.setContentView(R.layout.searchbook_dialog);

        dialog.show();
        dialog.setOwnerActivity(AdminActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        final EditText searchName = (EditText) dialog.findViewById(R.id.searchBookName);


        final TextView bookCode = (TextView) dialog.findViewById(R.id.bookCode);
        final TextView bookName = (TextView) dialog.findViewById(R.id.bookName);
        final TextView author = (TextView) dialog.findViewById(R.id.author);
        final TextView bookPublisher = (TextView) dialog.findViewById(R.id.bookPublisher);

        final LinearLayout productDetail = (LinearLayout) dialog.findViewById(R.id.bookDetail);

        Button search = (Button) dialog.findViewById(R.id.search);
        Button exit = (Button) dialog.findViewById(R.id.exit);

        Button update = (Button) dialog.findViewById(R.id.update);
        Button delete = (Button) dialog.findViewById(R.id.delete);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    Book item = items.get(i);
                    String a = item.getBookName();
                    if (a.equals(searchName.getText().toString())) {
                        bookCode.setText(item.getBookCode());
                        bookName.setText(item.getBookName());
                        author.setText(item.getAuthor());
                        bookPublisher.setText(item.getBookPublisher());
                        productDetail.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    Book item = items.get(i);
                    String a = item.getBookName();
                    if (a.equals(searchName.getText().toString())) {
                        updateBook(item.getBookCode(), item.getBookName(), item.getAuthor(), item.getBookPublisher());
                        dialog.dismiss();
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    Book item = items.get(i);
                    String a = item.getBookName();
                    if (a.equals(searchName.getText().toString())) {
                        deleteBook(item.getBookCode());
                        dialog.dismiss();
                    }
                }
            }
        });

    }

    public void deleteBook(String bookcode) {
        mBook.child(bookcode).removeValue();
    }

    public void updateBook(final String code, final String name, String author, String publisher) {

        final Dialog dialog = new Dialog(AdminActivity.this);
        dialog.setTitle("원하시는 메뉴를 선택해 주세요");
        dialog.setContentView(R.layout.update_book);

        dialog.show();
        dialog.setOwnerActivity(AdminActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        final EditText bookCode = (EditText) dialog.findViewById(R.id.bookCode);
        bookCode.setText(code);
        final EditText bookName = (EditText) dialog.findViewById(R.id.bookName);
        bookName.setText(name);
        final EditText author_e = (EditText) dialog.findViewById(R.id.author);
        author_e.setText(author);
        final EditText bookPublisher = (EditText) dialog.findViewById(R.id.bookPublisher);
        bookPublisher.setText(publisher);


        Button updateBtn = (Button) dialog.findViewById(R.id.update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book updateList = new Book(bookCode.getText().toString(), bookName.getText().toString(), author_e.getText().toString(), bookPublisher.getText().toString());
                Map<String, Object> updateListMap = updateList.toMap();
                mBook.child(code).updateChildren(updateListMap);
                dialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }
}
