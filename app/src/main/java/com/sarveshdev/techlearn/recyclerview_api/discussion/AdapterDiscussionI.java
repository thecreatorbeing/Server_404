package com.sarveshdev.techlearn.recyclerview_api.discussion;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sarveshdev.techlearn.R;


import de.hdodenhof.circleimageview.CircleImageView;

@Deprecated
public class AdapterDiscussionI extends FirebaseRecyclerAdapter<ModelDiscussion, AdapterDiscussionI.MyViewHolderI>{
    public AdapterDiscussionI(@NonNull FirebaseRecyclerOptions<ModelDiscussion> options){
        super(options);
    }//don't touch this!

    @NonNull
    @Override
    public MyViewHolderI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_discussion, parent, false);

        return new MyViewHolderI(root);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolderI holder, int position, @NonNull ModelDiscussion model) {
        holder.progressBar.setIndeterminate(true);

        holder.query.setText(model.getQuery());
        holder.query.setTextColor(Color.parseColor("#ffffff"));
        holder.name.setText(model.getUserName());

        /*holder.rootCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChattingActivity.class);
                intent.putExtra(IntentConstants.QUERY_ASKER_NAME, model.getUserName());
                intent.putExtra(IntentConstants.QUERY_ASKER_PHOTO_URL, model.getUserIconUrl());
                intent.putExtra(IntentConstants.QUERY_ASKED, model.getQuery());
                intent.putExtra(IntentConstants.CURRENT_COMMENT_SECTION_ID, model.getCommentSectionId());

            }
        });*/


        Log.d("sarvesh_log", "---------------------");
        Log.d("sarvesh_log", "onBindViewHolder: " + model.getCommentSectionId());
        Log.d("sarvesh_log", "onBindViewHolder: " + model.getUserIconUrl());
        Log.d("sarvesh_log", "onBindViewHolder: " + model.getUserName());
        Log.d("sarvesh_log", "onBindViewHolder: " + model.getQuery());
        Log.d("sarvesh_log", "---------------------");

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

    public interface IModelDiscussion {
        void onItemClicked(ModelDiscussion model);
    }

    public class MyViewHolderI extends RecyclerView.ViewHolder{

        CircleImageView circleImage;
        TextView query, name;
        ProgressBar progressBar;

        CardView rootCardView;

        public MyViewHolderI(@NonNull View itemView) {
            super(itemView);

            rootCardView = itemView.findViewById(R.id.cardDiscussionRootView);
            circleImage = itemView.findViewById(R.id.cardDiscussionUserImage);
            query = itemView.findViewById(R.id.cardDiscussionTextQuery);
            progressBar = itemView.findViewById(R.id.cardDiscussionProgressBar);
            name = itemView.findViewById(R.id.cardDiscussionTextName);
        }
    }
}
