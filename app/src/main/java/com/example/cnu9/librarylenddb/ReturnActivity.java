package com.example.cnu9.librarylenddb;

//빌린 목록 조회 페이지
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReturnActivity extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mProduct = mDatabase.child("Book");

    ListView listView;
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
        setContentView(R.layout.activity_return);


        listView = (ListView) findViewById(R.id.productListView);

        adapter = new BookAdapter();


        mProduct.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.allRemove(); //리스트 내부를 모두 지웠다가 아래 for문으로 다시 생성
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookCode = snapshot.child("BookCode").getValue(String.class);
                    String bookName = snapshot.child("BookName").getValue(String.class);
                    String author = snapshot.child("Author").getValue(String.class);
                    String bookPublisher = snapshot.child("BookPublisher").getValue(String.class);
                    adapter.addItem(new Book(bookCode, bookName, author, bookPublisher));
                    listView.setAdapter(adapter); //리스트 뷰에 어댑터 객체 설정
                }
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //저장한 값을 이용하여 어댑터에 각 아이템 추가

//
////
////        //전체 상품보기에서 해당하는 상품 클릭시 메인으로 돌아와 선택된 상품 바코드 출력
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Product item = (Product) adapter.getItem(position);
//////                item.getBacode();//이게 바코드 받아오는 코드
////                String a = item.getBacode();
////                Intent intent = new Intent();
////                intent.putExtra("position",a);
////                setResult(RESULT_OK,intent);
////                finish();
////            }
////        });

    }
}