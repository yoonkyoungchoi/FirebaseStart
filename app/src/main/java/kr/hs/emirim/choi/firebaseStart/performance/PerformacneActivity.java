package kr.hs.emirim.choi.firebaseStart.performance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Trace;
import android.view.View;

import kr.hs.emirim.choi.firebaseStart.R;

public class PerformacneActivity extends AppCompatActivity implements View.OnClickListener {
    private Trace trace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trace = FirebasePerformance.getInctance().newTrace()
        trace
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performacne);
    }

    @Override
    public void onClick(View view) {

    }

}