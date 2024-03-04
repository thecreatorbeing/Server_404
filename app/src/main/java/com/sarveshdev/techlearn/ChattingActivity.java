package com.sarveshdev.techlearn;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.Query;
import com.sarveshdev.techlearn.constants.ApiConstants;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.constants.IntentConstants;
import com.sarveshdev.techlearn.dataNetworkCheck_api.ConnectionModel;
import com.sarveshdev.techlearn.dataNetworkCheck_api.DataConnectionObserverAPI;
import com.sarveshdev.techlearn.dialog_api.DialogApi;
import com.sarveshdev.techlearn.firebase.FirebaseApi;
import com.sarveshdev.techlearn.recyclerview_api.discussion.chatting.AdapterChatting;
import com.sarveshdev.techlearn.recyclerview_api.discussion.chatting.ModelChatting;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingActivity extends AppCompatActivity {

    RecyclerView recyclerViewChatting;

    AdapterChatting adapterChatting;

    Query currentCommentSectionNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);



        /*ArrayList<String> currentQuery = getIntent().getStringArrayListExtra(IntentConstants.CURRENT_QUERY_ARRAYLIST);

        assert currentQuery != null;
        //remember the sequence from sender Intent
        String name = currentQuery.get(0);
        String url = currentQuery.get(1);
        String query = currentQuery.get(2);
        String commentSectionId = currentQuery.get(3);*/

            String name = getIntent().getStringExtra(IntentConstants.QUERY_ASKER_NAME);
            String url = getIntent().getStringExtra(IntentConstants.QUERY_ASKER_PHOTO_URL);
            String query = getIntent().getStringExtra(IntentConstants.QUERY_ASKED);
            String commentSectionId = getIntent().getStringExtra(IntentConstants.CURRENT_COMMENT_SECTION_ID);

            assert commentSectionId != null;

            currentCommentSectionNode = FirebaseConstants.COMMENT_SECTION_NODE.child(commentSectionId);
            /*------------------------------------------------------------------------------------------------*/
            ((TextView) findViewById(R.id.actChattingTextName)).setText(name);

            ProgressBar progressBar = findViewById(R.id.actChattingProgressBar);
            progressBar.setIndeterminate(true);

            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.logo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(((CircleImageView) findViewById(R.id.actChattingUserImage)));

            ((TextView) findViewById(R.id.actChattingTextQuery)).setText(query);

            /*------------------------------------------------------------------------------------------------*/

            try {
                recyclerViewChatting = findViewById(R.id.actChattingRecyclerview);
                FirebaseRecyclerOptions<ModelChatting> chattingOptions = new FirebaseRecyclerOptions.Builder<ModelChatting>()
                        .setQuery(currentCommentSectionNode.getRef(), ModelChatting.class)
                        .build();
                recyclerViewChatting.setLayoutManager(new LinearLayoutManager(this));
                adapterChatting = new AdapterChatting(chattingOptions);
                recyclerViewChatting.setAdapter(adapterChatting);
                adapterChatting.startListening();
            } catch(Exception ex){
                Log.d("sarvesh_err", "error: " + ex.getMessage());
            }

            /*------------------------------------------------------------------------------------------------*/

            findViewById(R.id.actChattingBtnSend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(FirebaseApi.getSignedInUser()!=null) {
                        EditText msg = findViewById(R.id.actChattingEdittextMessage);

                        if (!msg.getText().toString().trim().isEmpty()) {
                            String userImgUrl = String.valueOf(FirebaseApi.getSignedInUser().getPhotoUrl());
                            String userName = FirebaseApi.getSignedInUser().getDisplayName();
                            currentCommentSectionNode.getRef().push().setValue(
                                    new ModelChatting(userName, msg.getText().toString().trim(), userImgUrl)
                            );
                            msg.getText().clear();// empty the EditText
                        } else {
                            Toast.makeText(ChattingActivity.this, "enter your message firstly!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.actChattingRecyclerview) , "Visitors cannot chat!", Snackbar.LENGTH_LONG)
                                .setAnchorView(findViewById(R.id.actChattingConstrainLayoutBottom))
                                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                                .show();
                    }
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
                                Toast.makeText(ChattingActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
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

        }//onCreate() ended!

        @Override
        protected void onDestroy() {
            super.onDestroy();
            adapterChatting.stopListening();
        }
}