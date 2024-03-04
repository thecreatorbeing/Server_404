package com.sarveshdev.techlearn;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sarveshdev.techlearn.constants.ApiConstants;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.constants.IntentConstants;
import com.sarveshdev.techlearn.dataNetworkCheck_api.ConnectionModel;
import com.sarveshdev.techlearn.dataNetworkCheck_api.DataConnectionObserverAPI;
import com.sarveshdev.techlearn.dialog_api.DialogApi;
import com.sarveshdev.techlearn.recyclerview_api.videos.VideoModel;

public class ViewVideoActivity extends AppCompatActivity {


    Query query;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        progressBar = findViewById(R.id.actViewTiktokProgress);
        progressBar.setIndeterminate(true);

        Intent intent = getIntent();

        String tikTitle = intent.getStringExtra(IntentConstants.TIKTOK_TITLE);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(tikTitle);

        query = FirebaseConstants.QUERY_TIKTOK_NODE.orderByChild("title").equalTo(tikTitle);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VideoModel videoModel = new VideoModel();
                Log.d("fire-err", snapshot.toString());
                try {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        videoModel = s.getValue(VideoModel.class);
                    }

                    assert videoModel != null;
                    ((TextView) findViewById(R.id.actViewTiktokTextView)).setText(videoModel.getTitle());

                    ImageView playPauseImg = findViewById(R.id.actViewTiktokImgPause);
                    VideoView videoView = findViewById(R.id.actViewTiktokVideoView);
                    videoView.setVideoURI(Uri.parse(videoModel.getVideoUrl()));
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            progressBar.setVisibility(View.GONE);
//                            playPauseImg.setImageResource(android.R.drawable.ic_media_pause);
                            playPauseImg.setVisibility(View.INVISIBLE);
                        }
                    });

                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });

                    videoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (videoView.isPlaying()) {
                                videoView.pause();
                                playPauseImg.setVisibility(View.VISIBLE);
                            } else {
                                videoView.start();
                                playPauseImg.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    /*TiktokModel finalTiktokModel = videoModel;
                    videoView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            TextView textDescription = new TextView(getBaseContext());
                            textDescription.setText(finalTiktokModel.getDesc());
                            textDescription.setBackgroundResource(R.drawable.shape_round_corner_hollow);

                            AlertDialog dialog = new AlertDialog.Builder(getBaseContext())
                                    .setTitle("Description")
                                    .setMessage(finalTiktokModel.getDesc())
//                                    .setView(textDescription)
                                    .setCancelable(true)
                                    .create();

                            dialog.show();

                            return false;
                        }
                    });*/

                } catch (Exception e){
                    Toast.makeText(ViewVideoActivity.this, "error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewVideoActivity.this, "Database error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                            Toast.makeText(ViewVideoActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
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

    }
}