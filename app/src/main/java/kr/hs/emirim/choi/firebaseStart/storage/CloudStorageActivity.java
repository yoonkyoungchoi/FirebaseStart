package kr.hs.emirim.choi.firebaseStart.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import kr.hs.emirim.choi.firebaseStart.R;

public class CloudStorageActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_storage);

        findViewById(R.id.upload_btn).setOnClickListener(this);
        findViewById(R.id.download_btn).setOnClickListener(this);
        findViewById(R.id.meta_info_btn).setOnClickListener(this);
        findViewById(R.id.delete_btn).setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, REQUEST_CODE);
                Toast.makeText(this,"안드로이드 6부터 일부 권한에 대한 사용자 동의가 필요합니다.",Toast.LENGTH_LONG).show();

                findViewById(R.id.upload_btn).setEnabled(false);
                findViewById(R.id.download_btn).setEnabled(false);
                findViewById(R.id.meta_info_btn).setEnabled(false);
                findViewById(R.id.delete_btn).setEnabled(false);


            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.upload_btn :
                intent = new Intent(this,UploadActivity.class);
                break;
            case R.id.download_btn :
                intent = new Intent(this,DownloadActivity.class);
                break;
            default:
                break;
        }
        if (intent!=null) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findViewById(R.id.upload_btn).setEnabled(true);
                    findViewById(R.id.download_btn).setEnabled(true);
                    findViewById(R.id.meta_info_btn).setEnabled(true);
                    findViewById(R.id.delete_btn).setEnabled(true);
                }
                break;
            default:

        }

    }
}