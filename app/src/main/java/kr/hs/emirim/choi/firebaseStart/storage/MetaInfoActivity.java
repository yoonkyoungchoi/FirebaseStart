package kr.hs.emirim.choi.firebaseStart.storage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import kr.hs.emirim.choi.firebaseStart.R;

public class MetaInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_info);

        findViewById(R.id.meta_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetaData();
            }
        });
    }

    private void getMetaData() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference dogReference = storageReference.child("storage/images.jpeg");

        dogReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String metadata = storageMetadata.getName() + "\n" +
                        storageMetadata.getPath() + "\n" +
                        storageMetadata.getBucket();

                TextView metatxt = findViewById(R.id.meta_info_text);
                metatxt.setText(metadata);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}