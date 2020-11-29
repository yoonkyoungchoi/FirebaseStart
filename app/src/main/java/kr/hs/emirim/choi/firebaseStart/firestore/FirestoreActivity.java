package kr.hs.emirim.choi.firebaseStart.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import kr.hs.emirim.choi.firebaseStart.R;

public class FirestoreActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "파베 TAG";
    FirebaseFirestore db = null;
    private EventListener<DocumentSnapshot> eventListener = null;
    private ListenerRegistration eventQueryListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        findViewById(R.id.adddatabtn).setOnClickListener(this);
        findViewById(R.id.setdatabtn).setOnClickListener(this);
        findViewById(R.id.deletedocbtn).setOnClickListener(this);
        findViewById(R.id.selectdatabtn).setOnClickListener(this);
        findViewById(R.id.select_where_data_btn).setOnClickListener(this);
        findViewById(R.id.listener_data_btn).setOnClickListener(this);
        findViewById(R.id.listener_query_data).setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (eventQueryListener!=null) {
            eventQueryListener.remove();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adddatabtn :
                addData();
                break;
            case R.id.setdatabtn :
                setData();
                break;
            case R.id.deletedocbtn :
                deleteDoc();
                break;
            case R.id.deletefieldbtn :
                deleteField();
                break;
            case R.id.selectdatabtn :
                selectData();
                break;
            case R.id.select_where_data_btn :
                selectWhereData();
                break;
            case R.id.listener_data_btn :
                listenerDoc();
                break;
            case R.id.listener_query_data :
                listenerQuery();
        }
    }

    private void listenerQuery() {
        Log.e(TAG,"리스너 쿼리 도큐먼트 시작");

        if(eventQueryListener != null) {
            return;
        }

        eventQueryListener =
                db.collection("users")
                        .whereEqualTo("id","hong")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                Log.e(TAG,"ListenerQueryDoc in 1");

                                if(error!=null) {
                                    Log.e(TAG,"error : "+error);
                                    return;
                                }

                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    Log.e(TAG,"Type"+dc.getType());
                                    switch (dc.getType()) {
                                        case ADDED:
                                            Log.e(TAG,"New city");
                                    }
                                }
                            }
                        });

    }

    private void listenerDoc() {
        final DocumentReference docRef = db.collection("users").document("user_info");

        if(eventListener != null) {
            return;
        }
        eventListener = new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Listen failed", error);
                }

                if (value != null && value.exists()) {
                    Log.e(TAG, "현재 데이터: " + value.getData());
                } else {
                    Log.e(TAG, "현재 데이터 : null");
                }
            }
        };
        docRef.addSnapshotListener(eventListener);
    }

    private void selectWhereData() {
        db.collection("users")
                .whereEqualTo("age",25)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Document Snapshot data: "+document.getData());
                                UserInfo userInfo = document.toObject(UserInfo.class);
                            }
                        } else {
                            Log.d(TAG, "해당 데이터가 없습니다");
                        }
                    }
                });
    }

    private void selectData() {
        final DocumentReference docRef = db.collection("users")
                .document("user_info");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Log.d(TAG, "Document Snapshot data: "+documentSnapshot.getData());
                        UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);

                    } else {
                        Log.d(TAG, "해당 데이터가 없습니다");
                    }
                } else {
                    Log.e("TAG","get failed with "+task.getException());
                }
            }
        });
    }

    private void deleteField() {
        DocumentReference docRef = db.collection("users")
                .document("user_info");
        Map<String, Object> updates = new HashMap<>();
        updates.put("address", FieldValue.delete());
        docRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG,"문서 field 삭제");
                    }
                });
    }

    private void deleteDoc() {
        db.collection("users")
                .document("userInfo")
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Document Snapshot을 정상적으로 삭제");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Document Error! "+e.getMessage());
            }
        });

    }

    private void setData() {
        Map<String, Object> member = new HashMap<>();
        member.put("name","이승민");
        member.put("address","서울시");
        member.put("age",18);
        member.put("id","july");
        member.put("pwd","haha!");

        db.collection("users").document("user_info").set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Document Snapshot을 정상적으로 추가");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Document Error! "+e.getMessage());
            }
        });
    }

    private void addData() {
        Map<String, Object> member = new HashMap<>();
        member.put("name","홍길동");
        member.put("address","수원시");
        member.put("age",25);
        member.put("id","hong");
        member.put("pwd","hello!");

        db.collection("users").add(member).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"Document ID = "+documentReference.get());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Document Error! "+e.getMessage());
            }
        });
    }
}