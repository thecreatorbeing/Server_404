package com.sarveshdev.techlearn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sarveshdev.techlearn.constants.ApiConstants;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.dataNetworkCheck_api.ConnectionModel;
import com.sarveshdev.techlearn.dataNetworkCheck_api.DataConnectionObserverAPI;
import com.sarveshdev.techlearn.dialog_api.DialogApi;
import com.sarveshdev.techlearn.firebase.AdminModel;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    ImageView centerLogo, splashImg;
    FirebaseAuth firebaseAuth;
    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;
    AlertDialog networkErrDialog;

    ActivityResultLauncher<Intent> mGoogleSignInLauncher;

    private boolean canShowSignInDialog = true;
    Thread dialogThread;
    AlertDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        centerLogo = findViewById(R.id.actSplashCenterLogo);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        networkErrDialog = DialogApi.createNetworkErrorDialog(this);



        dialogThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
//                            while(true){
                            Log.d("dialogThread", "thread is running!");
                            canShowSignInDialog = false;
                            Thread.sleep(5000);
                            if(currentUser==null){canShowSignInDialog = true; showDialogForUserNotSignUp();}
                            else {canShowSignInDialog = false;}
//                            }
                        } catch (Exception e){
                            Log.d("dialodThread", Objects.requireNonNull(e.getLocalizedMessage()));
                        }
                    }
                }, 5000);//loop after 5 seconds!
                Looper.loop();
            }//run() ended!
        };

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);
        signInClient.revokeAccess();

        // Create an instance of ActivityResultLauncher
        mGoogleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w("signin-failed", "Google sign in failed", e);
                        }
                    }
                });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Check if user is signed in (non-null) and update UI accordingly.
                    if(currentUser == null){
                        //Toast.makeText(SplashActivity.this, "no user found!", Toast.LENGTH_SHORT).show();
                        showDialogForUserNotSignUp();
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//run() ended!
        };//runnable ended!

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    runOnUiThread(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//run() ended!
        }.start();

        Dialog networkErrDialog = DialogApi.createNetworkErrorDialog(this);
        new DataConnectionObserverAPI(getBaseContext()).observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(ConnectionModel connection) {
                if(connection.getIsConnected()){
                    if(connection.getType() != ApiConstants.DataNetwork.FROM_MOBILE_DATA
                            && connection.getType() != ApiConstants.DataNetwork.FROM_WIFI){
                        try {
                            if (!networkErrDialog.isShowing()){
                                networkErrDialog.show();
                            }
                        } catch(Exception e) {
                            Toast.makeText(SplashActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (networkErrDialog.isShowing()){
                            networkErrDialog.dismiss();
                        }
                    }
                } else {
                    if (!networkErrDialog.isShowing()) {
                        networkErrDialog.show();
                    }
                }
            }//onChanged() ended!
        });//observe() ended!

    }//onCreate ended!

    private void showDialogForUserNotSignUp(){
        loginDialog = new AlertDialog.Builder(this).create();
        View view = this.getLayoutInflater().inflate(R.layout.dialog_login, null);
        loginDialog.setView(view);
        loginDialog.setCancelable(false);
        view.findViewById(R.id.dialogLoginBtnGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                loginDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialogLoginBtnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                finish();
            }
        });
        view.findViewById(R.id.dialogLoginBtnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText id = view.findViewById(R.id.dialogLoginEditTextVisitorId);
                EditText password = view.findViewById(R.id.dialogLoginEditTextVisitorPassword);

                assert id != null && password!= null;

                if (id.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(SplashActivity.this, "enter valid visitor credentials", Toast.LENGTH_SHORT).show();
//                        if(!dialogThread.isAlive()){
//                            dialogThread.start();
//                        }
                } else {
                    if (id.getText().toString().trim().equals("admin") || password.getText().toString().trim().equals("Pass@123")) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(SplashActivity.this, "wrong credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginDialog.show();
    }//showDialogForUserNotSignUp() ended!

    private void signIn(){
        DataConnectionObserverAPI dataObserver = new DataConnectionObserverAPI(getBaseContext());
        dataObserver.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(ConnectionModel connection) {
                if(connection.getIsConnected()){
                    if(connection.getType() == ApiConstants.DataNetwork.FROM_MOBILE_DATA
                            || connection.getType() == ApiConstants.DataNetwork.FROM_WIFI){
                        try {
                            if(networkErrDialog.isShowing()){networkErrDialog.dismiss();}
//                                startActivityForResult(signInIntent, ApiConstants.SignIn.SIGN_IN);
                            mGoogleSignInLauncher.launch(signInClient.getSignInIntent());
//                                Toast.makeText(SplashActivity.this, "trying!", Toast.LENGTH_SHORT).show();
                        } catch(Exception e) {
                            Toast.makeText(SplashActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        networkErrDialog.setCanceledOnTouchOutside(false);
                        networkErrDialog.show();
//                        Toast.makeText(SplashActivity.this, "data network not available!", Toast.LENGTH_SHORT).show();
//                        showDialogForUserNotSignUp();
                    }
                }
            }//onChanged() ended!
        });//observe() ended!
    }//signIn() ended!

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ApiConstants.SignIn.SIGN_IN && resultCode==RESULT_OK){

            if (loginDialog.isShowing()){loginDialog.dismiss();}

            //Toast.makeText(this, "sign-in successful!", Toast.LENGTH_SHORT).show();
            //sign in successful ...proceed for firebase
            DataConnectionObserverAPI dataConnectionObserver= new DataConnectionObserverAPI(getBaseContext());
            dataConnectionObserver.observe(this, new Observer<ConnectionModel>() {
                @Override
                public void onChanged(@Nullable ConnectionModel connection) {
                    if (Objects.requireNonNull(connection).getIsConnected()) {
                        if(connection.getType() == ApiConstants.DataNetwork.FROM_MOBILE_DATA
                                || connection.getType() == ApiConstants.DataNetwork.FROM_WIFI){
                            if(networkErrDialog.isShowing()){networkErrDialog.dismiss();}
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try{
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                firebaseAuthWithGoogle(account.getIdToken());
                            }catch(Exception e){
                                //sign-in failed handle properly!
                                //Toast.makeText(getBaseContext(), "sign-in failed!", Toast.LENGTH_SHORT).show();
                                showDialogForUserNotSignUp();
                            }
                        }else{
                            networkErrDialog.setCanceledOnTouchOutside(false);
                            networkErrDialog.show();
//                            Toast.makeText(getBaseContext(), "data connection unavailable!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        networkErrDialog.setCanceledOnTouchOutside(false);
                        networkErrDialog.show();
                        //Toast.makeText(getBaseContext(), "data connection unavailable!", Toast.LENGTH_SHORT).show();
                    }
                }//onChanged() ended!
            });//observe() ended!
        }else{
            //Toast.makeText(this, "sign-in failed!", Toast.LENGTH_SHORT).show();
            showDialogForUserNotSignUp();
        }
    }//onActivityResult() ended!

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            canShowSignInDialog = false;
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            //Toast.makeText(SplashActivity.this, "current user: "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            //sign-in successful use sign in credentials from here!
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }else{
                            //sign in failed.... handle from here!
                            canShowSignInDialog = true;
                            Toast.makeText(SplashActivity.this, "sign-in failed!", Toast.LENGTH_SHORT).show();
                        }
                    }//onComplete() ended!
                });
    }//firebaseAuthWithGoogle() ended!
}