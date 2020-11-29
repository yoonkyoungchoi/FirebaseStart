package kr.hs.emirim.choi.firebaseStart.storage;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.hs.emirim.choi.firebaseStart.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "download";
    private File localfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        findViewById(R.id.localfile_download).setOnClickListener(this);
        findViewById(R.id.firebase_download).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.localfile_download :
                showDownloadLocalFileImageView();
                break;
            case R.id.firebase_download :
                showFirebaseUiDownloadImageView();
                break;
            default:
                break;
        }
    }

    private void showFirebaseUiDownloadImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("storage/images.jpeg");
        ImageView imageView = findViewById(R.id.storageImg);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .centerCrop()
                .override(700,500)
                .into(imageView);
    }

    private void showDownloadLocalFileImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("storage/images.jpeg");

        try {
            localfile = File.createTempFile("images","jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        pathReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                long fileSize = taskSnapshot.getTotalByteCount();
                Log.d(TAG,"File Size : "+fileSize);
                Log.d(TAG,"File Name : "+localfile.getAbsolutePath());

                ImageView imageView = findViewById(R.id.storageImg);
                Glide.with(DownloadActivity.this)
                        .load(new File(localfile.getAbsolutePath()))
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onFailure in");
                e.printStackTrace();
            }
        });
    }
}
