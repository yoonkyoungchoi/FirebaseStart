package kr.hs.emirim.choi.firebaseStart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.w3c.dom.Text;

public class SignedInActivity extends AppCompatActivity implements View.OnClickListener {
    private IdpResponse mIdpRespones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signedin);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            finish();
        }
        mIdpRespones = IdpResponse.fromResultIntent(getIntent());

        setContentView(R.layout.activity_signedin);
        populateProfile();
        populateIdpToken();

        Button signoutbtn = findViewById(R.id.sign_out);
        signoutbtn.setOnClickListener(this);

        Button deleteuser = findViewById(R.id.delete_account);
        deleteuser.setOnClickListener(this);
    }

    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView emailtxt = (TextView)findViewById(R.id.user_email);
        emailtxt.setText(
                TextUtils.isEmpty(user.getEmail()) ? "NO email" : user.getEmail());

        TextView usernametext = (TextView)findViewById(R.id.user_display_name);
        usernametext.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "NO display name" : user.getDisplayName());

        StringBuilder providerList = new StringBuilder(100);
        providerList.append("ProviderData used: ");

        if (user.getProviderData() == null || user.getProviderData().isEmpty()){
            providerList.append("none");
        }
        else{
            for(UserInfo profile : user.getProviderData()){
                String providerId = profile.getProviderId();
                if(GoogleAuthProvider.PROVIDER_ID.equals(providerId)){
                    providerList.append("Google");
                }
                else if(FacebookAuthProvider.PROVIDER_ID.equals(providerId)){
                    providerList.append("Facebook");
                }
                else if(TwitterAuthProvider.PROVIDER_ID.equals(providerId)){
                    providerList.append("Twitter");
                }
                else if(EmailAuthProvider.PROVIDER_ID.equals(providerId)){
                    providerList.append("Email");
                }
                else{
                    providerList.append(providerId);
                }
            }
        }
        TextView userenabled = (TextView)findViewById(R.id.user_enabled_providers);
        userenabled.setText(providerList);

    }
    private void populateIdpToken(){
        String token = null;
        if (mIdpRespones != null){
            token = mIdpRespones.getIdpToken();
        }
        if (token == null){
            findViewById(R.id.idp_token).setVisibility(View.GONE);
        }else{
            ((TextView)findViewById(R.id.idp_token)).setText(token);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_out:
                signOut();
                break;
            case R.id.delete_account:
                //TODO:deleteAccountCliked();
                break;
            default:
                break;
        }
    }
    private void deleteAccountClicked(){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("정말 진짜 리얼 찐삭제?")
                    .setPositiveButton("응, 찐 리얼 진짜 삭제할래!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteAccount();
                                }
                            })
                    .setNegativeButton("아니 그냥 냅둘래", null)
                    .create();
            dialog.show();
        }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "계정 삭제 성공!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "계정 삭제 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            finish();
                        } else {

                        }
                    }
                });
    }
}
