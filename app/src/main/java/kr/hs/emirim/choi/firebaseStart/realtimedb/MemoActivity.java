package kr.hs.emirim.choi.firebaseStart.realtimedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import kr.hs.emirim.choi.firebaseStart.AuthActivity;
import kr.hs.emirim.choi.firebaseStart.R;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener, MemoViewListener{

    private static final String TAG = "파이어베이스";
    private ArrayList<MemoItem> memoItems = null;
    private MemoAdapter memoAdapter = null;
    private String username = null;
    private String uid = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        init();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        addChildEvent();
        addValueEventListener();
    }

    private void addValueEventListener() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"값 : "+snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onItemClick(int position, View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.memobtn :
                regMemo();
                break;
            case R.id.reguserbtn :
                writeNewUser();
                break;
        }
    }

    private void writeNewUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String name = user.getDisplayName();
            Toast.makeText(getApplicationContext(), "표시이름: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            String email = user.getEmail();
            String uid = user.getUid();

            Log.d(TAG,name);
            Log.d(TAG,email);
            Log.d(TAG,uid);

            UserInfo userInfo = new UserInfo();
            userInfo.setUserpwd("1234");
            userInfo.setUsername(name);
            userInfo.setEmailaddr(email);

            databaseReference.child("users").child(uid).setValue(userInfo);

        } else {
            Log.d(TAG,"user null");
        }

    }

    private void addChildEvent() {
        databaseReference.child("memo").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Log.d(TAG,"Child 추가 이벤트 발생");
                //Toast.makeText(getApplicationContext(), "새 글이 등록되었습니다!",Toast.LENGTH_SHORT).show();
                MemoItem item = snapshot.getValue(MemoItem.class);
                memoItems.add(item);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        memoItems = new ArrayList<>();
        username = "user_"+new Random().nextInt(1000);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            this.uid = user.getUid();
        }else{
            Toast.makeText(this, "로그인 하세요!", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }

    }
    private void initView() {
        Button regbtn = findViewById(R.id.memobtn);
        regbtn.setOnClickListener(this);

        Button userbtn = findViewById(R.id.reguserbtn);
        userbtn.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.memolist);
        memoAdapter = new MemoAdapter(this, memoItems, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(memoAdapter);
    }

    private void regMemo() {
        if(uid == null){
            Toast.makeText(this, "로그인 하세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText titleedit = findViewById(R.id.memotitle);
        EditText contentedit = findViewById(R.id.memocontents);
        if(titleedit.getText().toString().length() == 0 ||
                contentedit.getText().toString().length() ==0 ) {
            Toast.makeText(this, "메모 제목 또는 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_SHORT).show();

            return;

        }
        MemoItem item = new MemoItem();
        item.setUser(this.username);
        item.setTitle(titleedit.getText().toString());
        item.setMemocontents(contentedit.getText().toString());

        databaseReference.child("memo").child(uid).push().setValue(item);

        //memoItems.add(item);
        //memoAdapter.notifyDataSetChanged();

    }


}