package com.sarveshdev.techlearn;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sarveshdev.techlearn.constants.ApiConstants;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.dataNetworkCheck_api.ConnectionModel;
import com.sarveshdev.techlearn.dataNetworkCheck_api.DataConnectionObserverAPI;
import com.sarveshdev.techlearn.dialog_api.DialogApi;

public class SplashActivity extends AppCompatActivity {


    AlertDialog networkErrDialog, loginDialog;
    ActivityResultLauncher<Intent> mGoogleSignInLauncher;
    GoogleSignInClient signInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        networkErrDialog = DialogApi.createNetworkErrorDialog(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//run() ended!
        };//runnable ended!

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
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    runOnUiThread(runnable);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }//run() ended!
                        }.start();
                    }
                } else {
                    if (!networkErrDialog.isShowing()) {
                        networkErrDialog.show();
                    }
                }
            }//onChanged() ended!
        });//observe() ended!

    }

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

}