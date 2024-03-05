package com.sarveshdev.techlearn;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.firebase.FirebaseApi;

import java.util.Random;

public class TakeAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        if (FirebaseApi.getSignedInUser() == null){
            //user is admin!
            findViewById(R.id.studentLinearLayout).setVisibility(View.GONE);

            findViewById(R.id.actTakeAttendanceBtnGenerate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long number = generateRandomInt(0, 999999);
                    FirebaseConstants.ATTENDANCE_RANDOM_NUMBER_NODE.setValue(number).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ((Button) findViewById(R.id.actTakeAttendanceBtnGenerate)).setText(String.valueOf(number));


                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    ((Button) findViewById(R.id.actTakeAttendanceBtnGenerate)).setText("click to generate");
                                    FirebaseConstants.ATTENDANCE_RANDOM_NUMBER_NODE.setValue(-1);
                                }
                            };

                            (new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(5000);
                                        runOnUiThread(runnable);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }).start();
                        }
                    });
                }
            });

        } else {//user is a student!
            findViewById(R.id.adminLinearLayout).setVisibility(View.GONE);

            final long[] secretNum = {-1};
            FirebaseConstants.ATTENDANCE_RANDOM_NUMBER_NODE.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        secretNum[0] = snapshot.getValue(Long.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            findViewById(R.id.actTakeAttendanceBtnupload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText input = findViewById(R.id.editTextAttendanceNumber);

                    if (input.getText().toString().isEmpty()){
                        Toast.makeText(TakeAttendance.this, "enter properly!", Toast.LENGTH_SHORT).show();
                    } else {
                        long entered = Long.parseLong(input.getText().toString());

                        if(secretNum[0]!=-1 && secretNum[0]==entered){
                            final long[] attendance = {0};
                            FirebaseConstants.ATTENDANCE_NODE.child(FirebaseApi.getSignedInUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Log.d("snapshot 121", snapshot.toString());
                                        attendance[0] = snapshot.getValue(Long.class);
                                        Log.d("snapshot 123", String.valueOf(attendance[0]));

                                        /*for (DataSnapshot s: snapshot.getChildren()) {
                                            Log.d("snapshot 123", s.toString());
                                            attendance[0] = s.getValue(Long.class);
                                            Log.d("snapshot 123", String.valueOf(attendance[0]));
                                        }*/
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Log.d("snapshot check", String.valueOf(attendance[0]));
                            int num = (int) (attendance[0] + 1);
                            Log.d("snapshot check", String.valueOf(num));
                            FirebaseConstants.ATTENDANCE_NODE.child(FirebaseApi.getSignedInUser().getUid()).setValue(
                                    num
                            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(TakeAttendance.this, "Attendance Registered Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(TakeAttendance.this, "Attendance Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

        }

    }

    public static int generateRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}