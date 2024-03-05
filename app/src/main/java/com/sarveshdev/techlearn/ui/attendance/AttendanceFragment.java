package com.sarveshdev.techlearn.ui.attendance;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sarveshdev.techlearn.R;
import com.sarveshdev.techlearn.TakeAttendance;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.firebase.FirebaseApi;

public class AttendanceFragment extends Fragment {


    public AttendanceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_attendance, container, false);

        if (FirebaseApi.getSignedInUser()!= null) {
            FirebaseConstants.ATTENDANCE_NODE.child(FirebaseApi.getSignedInUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d("snapshot", snapshot.toString());
                        String value = String.valueOf(snapshot.getValue(Integer.class));
                        Log.d("snapshot", "I got: " + value);
                        ((TextView) root.findViewById(R.id.fragAttendanceTextView)).setText(value);
                    } else {
                        FirebaseConstants.ATTENDANCE_NODE.child(FirebaseApi.getSignedInUser().getUid()).setValue(0);
                        ((TextView) root.findViewById(R.id.fragAttendanceTextView)).setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            ((TextView) root.findViewById(R.id.fragAttendanceTextView)).setText("You are an admin!");
        }

        root.findViewById(R.id.fragAttendanceBtnSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToLogOut(getContext());
            }
        });

        root.findViewById(R.id.fragAttendanceBtnAttendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TakeAttendance.class));
            }
        });

        return root;
    }

    private void showDialogToLogOut(Context context){
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle("Confirm Log-Out?")
                .setMessage("1}. This will delete all of your data from previous account.\n" +
                        "2}. Signing in will download data associated to new account.\n" +
                        "3}. App will close after proceeding.")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getContext(), "logged out successfully!", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();

//                        startActivity(new Intent(context, SplashActivity.class));//not required since app closes!!!
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }//showDialogToLogOut() ended!

}