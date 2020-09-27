package kr.hs.emirim.choi.firebaseStart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final  static String TAG = "파베Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("파베:main", "파베어스 버튼 눌림!");
        Button firebaseauthbtn = findViewById(R.id.firebaseauthbtn);
        firebaseauthbtn.setOnClickListener(this) ;
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
            default:
                break;
        }
    }
}