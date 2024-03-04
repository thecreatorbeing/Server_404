package com.sarveshdev.techlearn.ui.notifications;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarveshdev.techlearn.ChattingActivity;
import com.sarveshdev.techlearn.R;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.constants.IntentConstants;
import com.sarveshdev.techlearn.databinding.FragmentNotificationsBinding;
import com.sarveshdev.techlearn.firebase.FirebaseApi;
import com.sarveshdev.techlearn.recyclerview_api.discussion.ModelDiscussion;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ModelDiscussion> options;
    AdapterDiscussion recyclerAdapter;
    AlertDialog dialogAskQuery;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        root.findViewById(R.id.fragNotificationBtnAsk).setOnClickListener(askBtnClickListener());

        try{
            recyclerView = binding.fragNotificationRecyclerview;
            options = new FirebaseRecyclerOptions.Builder<ModelDiscussion>()
                    .setQuery(FirebaseConstants.QUERY_DISCUSSION_NODE, ModelDiscussion.class)
                    .build();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerAdapter = new AdapterDiscussion(options);
            recyclerView.setAdapter(recyclerAdapter);
            /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getContext(), ChattingActivity.class);

                    ArrayList<String> temp = new ArrayList<>(); //remember the sequence!
                    temp.add(FirebaseConstants.ARRAYLIST_QUERY_ASKER_NAMES.get(position));
                    temp.add(FirebaseConstants.ARRAYLIST_QUERY_ASKER_IMG_URLS.get(position));
                    temp.add(FirebaseConstants.ARRAYLIST_QUERIES_ASKED.get(position));
                    temp.add(FirebaseConstants.ARRAYLIST_COMMENT_SECTION_IDS.get(position));
                    intent.putStringArrayListExtra(IntentConstants.CURRENT_QUERY_ARRAYLIST, temp);

                    intent.putExtra(IntentConstants.CURRENT_COMMENT_SECTION_ID, FirebaseConstants.ARRAYLIST_COMMENT_SECTION_IDS.get(position));
                    intent.putExtra(IntentConstants.QUERY_ASKER_PHOTO_URL, FirebaseConstants.ARRAYLIST_QUERY_ASKER_IMG_URLS.get(position));
                    intent.putExtra(IntentConstants.QUERY_ASKER_NAME, FirebaseConstants.ARRAYLIST_QUERY_ASKER_NAMES.get(position));
                    intent.putExtra(IntentConstants.QUERY_ASKED, FirebaseConstants.ARRAYLIST_QUERIES_ASKED.get(position));
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {
                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }));*/
            recyclerAdapter.startListening();

        } catch (Exception e){
            Toast.makeText(getContext(), "error Occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    /*-----------------------------------------------------------------------------------------------------------*/

    public class AdapterDiscussion extends FirebaseRecyclerAdapter<ModelDiscussion, AdapterDiscussion.ViewHolderDiscussion> {
        public AdapterDiscussion(@NonNull FirebaseRecyclerOptions<ModelDiscussion> options){
            super(options);
        }//don't touch this!

        @NonNull
        @Override
        public AdapterDiscussion.ViewHolderDiscussion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_discussion, parent, false);

            return new ViewHolderDiscussion(root);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void onBindViewHolder(@NonNull AdapterDiscussion.ViewHolderDiscussion holder, int position, @NonNull ModelDiscussion model) {
            holder.progressBar.setIndeterminate(true);

            holder.query.setText(model.getQuery());
            holder.name.setText(model.getUserName());

            holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChattingActivity.class);
                    intent.putExtra(IntentConstants.QUERY_ASKER_NAME, model.getUserName());
                    intent.putExtra(IntentConstants.QUERY_ASKER_PHOTO_URL, model.getUserIconUrl());
                    intent.putExtra(IntentConstants.QUERY_ASKED, model.getQuery());
                    intent.putExtra(IntentConstants.CURRENT_COMMENT_SECTION_ID, model.getCommentSectionId());
                    startActivity(intent);
                }
            });

            /*FirebaseConstants.ARRAYLIST_COMMENT_SECTION_IDS.add(model.getCommentSectionId());
            FirebaseConstants.ARRAYLIST_QUERY_ASKER_IMG_URLS.add(model.getUserIconUrl());
            FirebaseConstants.ARRAYLIST_QUERY_ASKER_NAMES.add(model.getUserName());
            FirebaseConstants.ARRAYLIST_QUERIES_ASKED.add(model.getQuery());

            Log.d("sarvesh_log", "---------------------");
            Log.d("sarvesh_log", "onBindViewHolder: " + model.getCommentSectionId());
            Log.d("sarvesh_log", "onBindViewHolder: " + model.getUserIconUrl());
            Log.d("sarvesh_log", "onBindViewHolder: " + model.getUserName());
            Log.d("sarvesh_log", "onBindViewHolder: " + model.getQuery());
            Log.d("sarvesh_log", "---------------------");*/

            Glide.with(holder.circleImage.getContext())
                    .load(model.getUserIconUrl())
                    .placeholder(R.drawable.logo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            Toast.makeText(holder.circleImage.getContext(), "failed to load user-icon!", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.circleImage);

        }

        public class ViewHolderDiscussion extends RecyclerView.ViewHolder{

            CircleImageView circleImage;
            TextView query, name;
            ProgressBar progressBar;

            CardView rootCardView;

            public ViewHolderDiscussion(@NonNull View itemView) {
                super(itemView);

                rootCardView = itemView.findViewById(R.id.cardDiscussionRootView);
                circleImage = itemView.findViewById(R.id.cardDiscussionUserImage);
                query = itemView.findViewById(R.id.cardDiscussionTextQuery);
                progressBar = itemView.findViewById(R.id.cardDiscussionProgressBar);
                name = itemView.findViewById(R.id.cardDiscussionTextName);
            }
        }
    }
    /*-----------------------------------------------------------------------------------------------------------*/

    @Override
    public void onStart() {
        super.onStart();
//        Snackbar.make(binding.fragNotificationRecyclerview , "Loading Topics...", Snackbar.LENGTH_LONG)
//                .setAnchorView(binding.fragNotificationGuideline)
//                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
//                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        recyclerAdapter.stopListening();
        Log.d("sarvesh_log", "discussion onDestroy()");

    }

    private View.OnClickListener askBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseApi.getSignedInUser() != null){
                    dialogAskQuery = new AlertDialog.Builder(getContext()).create();

                    @SuppressLint("InflateParams")
                    View view = getLayoutInflater().inflate(R.layout.dialog_ask_query, null);
                    view.findViewById(R.id.dlogAskQueryBtnCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAskQuery.dismiss();
                        }
                    });
                    view.findViewById(R.id.dlogAskQueryBtnSubmit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String queryString = ((EditText) view.findViewById(R.id.dlogAskQueryEditTextEnterQuery)).getText().toString();

                            if (queryString.trim().isEmpty()){
                                Toast.makeText(getContext(), "please write your query!", Toast.LENGTH_SHORT).show();
                            } else {

                                String commentSectionId = FirebaseConstants.QUERY_DISCUSSION_NODE.push().getKey();
                                FirebaseConstants.QUERY_DISCUSSION_NODE.push()
                                        .setValue(new ModelDiscussion(
                                                Objects.requireNonNull(FirebaseApi.getSignedInUser().getPhotoUrl()).toString(),
                                                queryString.trim(),
                                                FirebaseApi.getSignedInUser().getDisplayName(),
                                                commentSectionId
                                        ))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Query uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                attachCommentSection(commentSectionId);
                                                dialogAskQuery.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "failed to ask query... please try later!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                    dialogAskQuery.setView(view);
                    dialogAskQuery.setCancelable(false);
                    CircleImageView circleImageView = view.findViewById(R.id.dlogAskQueryCircleImg);
                    Glide.with(requireContext())
                            .load(FirebaseApi.getSignedInUser().getPhotoUrl())
                            .placeholder(R.drawable.logo)
                            .into(circleImageView);
                    dialogAskQuery.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.logo)
                            .setTitle("Alert!")
                            .setMessage("Visitor accounts cannot ask questions!")
                            .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
        };
    }//onClickListener() ended!!!

    void attachCommentSection(String id){
        FirebaseConstants.COMMENT_SECTION_NODE.child(id).setValue("");
    }
}