package kr.hs.emirim.choi.firebaseStart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.hs.emirim.choi.firebaseStart.firestore.FirestoreActivity;
import kr.hs.emirim.choi.firebaseStart.realtimedb.MemoActivity;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final  static String TAG = "파베Main";
    private Button firebaseauthbtn = null;
    private Button firebaserealtimedbbtn = null;
    private Button firebasefirestorybtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("파베:main", "파베어스 버튼 눌림!");
        firebaseauthbtn = findViewById(R.id.firebaseauthbtn);
        firebaserealtimedbbtn = findViewById(R.id.firebaserealtimedbbtn);
        firebaseauthbtn.setOnClickListener(this);
        firebaserealtimedbbtn.setOnClickListener(this);
        firebasefirestorybtn = findViewById(R.id.firebasefirestorybtn);
        Button firebaseauthbtn = findViewById(R.id.firebaseauthbtn);
        firebaseauthbtn.setOnClickListener(this) ;
        firebasefirestorybtn.setOnClickListener(this);
        Log.d("파베:main", "mainActiovity 버튼객체 참조 연결하고 리스너 등록");
    }
    @Override
    public void onClick(View view) {
        Toast.makeText(this, "버튼 눌렸어요", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"파베어스 버튼 눌림!");

        switch (view.getId()){
            case R.id.firebaseauthbtn:
                Log.d(TAG, "파이어베이스 인증 버튼 눌림!");
                Intent intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
                break;
            case R.id.firebaserealtimedbbtn:
                Log.d(TAG, "파이어베이스 인증 버튼 눌림!");
                intent = new Intent(this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.firebasefirestorybtn:
                Log.d(TAG, "파이어베이스 스토어 버튼 눌림!");
                intent = new Intent(this, FirestoreActivity.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG, "모르는 클릭?");
                break;
        }
    }
}