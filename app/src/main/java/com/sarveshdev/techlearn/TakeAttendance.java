package com.sarveshdev.techlearn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                        }
                    });
                }
            });

        } else {//user is a student!
            findViewById(R.id.adminLinearLayout).setVisibility(View.GONE);



        }

    }

    public static int generateRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}