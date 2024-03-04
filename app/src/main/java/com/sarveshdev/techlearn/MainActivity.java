package com.sarveshdev.techlearn;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.sarveshdev.techlearn.constants.ApiConstants;
import com.sarveshdev.techlearn.dataNetworkCheck_api.ConnectionModel;
import com.sarveshdev.techlearn.dataNetworkCheck_api.DataConnectionObserverAPI;
import com.sarveshdev.techlearn.databinding.ActivityMainBinding;
import com.sarveshdev.techlearn.dialog_api.DialogApi;
import com.sarveshdev.techlearn.firebase.FirebaseApi;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

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
                            Toast.makeText(MainActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
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

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            ((TextView) findViewById(R.id.tool_textView)).setText(FirebaseApi.getSignedInUser().getDisplayName());

            Glide.with(getBaseContext())
                    .load(FirebaseApi.getSignedInUser().getPhotoUrl())
                    .placeholder(R.drawable.logo)
                    .centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(getBaseContext(), "failed to load user image!", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(binding.toolbarImageview);
        } else {
            ((TextView) findViewById(R.id.tool_textView)).setText(R.string.admin);
            binding.toolbarImageview.setImageResource(R.drawable.logo);
        }

    }

}