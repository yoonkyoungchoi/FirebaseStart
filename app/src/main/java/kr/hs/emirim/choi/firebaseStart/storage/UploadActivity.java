package kr.hs.emirim.choi.firebaseStart.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import kr.hs.emirim.choi.firebaseStart.R;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_SELECT_IMAGE = 777;
    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrient = null;

    private Button mButton = null;
    private ProgressBar mProgressBar = null;
    private ProgressBar mProgressBar2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        findViewById(R.id.upload_page_btn).setOnClickListener(this);
        mButton = findViewById(R.id.upload_page_btn);
        mButton.setOnClickListener(this);
        mButton.setEnabled(true);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mProgressBar2 = findViewById(R.id.progressBar2);
        getGallery();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                getImageNameToUri(uri);

                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    ImageView img = findViewById(R.id.showing);
                    img.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getImageNameToUri(Uri uri) {
        String[] proj = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION,
        };
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
        cursor.moveToFirst();

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(column_data);
        mImgTitle = cursor.getString(column_title);
        mImgOrient = cursor.getString(column_orientation);

    }

    private void getGallery() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= 19) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_page_btn :
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar2.setVisibility(View.VISIBLE);
                mButton.setEnabled(false);
                uploadFile(mImgPath);
                break;
        }
    }

    private void uploadFile(String aFilePath) {
        Uri file = Uri.fromFile(new File(aFilePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg").build();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        UploadTask uploadTask = storageReference.child("storage/"
                + file.getLastPathSegment()).putFile(file,metadata);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                mProgressBar2.setProgress((int)progress);
                Toast.makeText(UploadActivity.this,"Upload is : "+progress + "% done", Toast.LENGTH_SHORT).show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                Log.d("파베","Upload is Paused");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("파베","Upload Exception");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d("파베","Upload is Canceled..");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("파베","Upload is Success!");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        });
    }
}