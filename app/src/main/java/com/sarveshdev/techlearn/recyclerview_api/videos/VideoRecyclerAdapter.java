package com.sarveshdev.techlearn.recyclerview_api.videos;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sarveshdev.techlearn.R;


public class VideoRecyclerAdapter extends FirebaseRecyclerAdapter<VideoModel, VideoRecyclerAdapter.MyViewHolder> {

    public VideoRecyclerAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options) {
        super(options);
    }//don't touch this!

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_dashboard_recyclerview, parent, false);
        return new MyViewHolder(root);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, VideoModel videoModel) {
        holder.progressBar.setIndeterminate(true);

        holder.title.setText(videoModel.getTitle());

        Glide.with(holder.imageView.getContext())
                .load(videoModel.getThumbnailUrl())
                .placeholder(R.drawable.logo)
                //.centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        Toast.makeText(holder.imageView.getContext(), "failed to load!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        //holder.imageView.setImageDrawable(resource);
                        //Toast.makeText(holder.imageView.getContext(), "success!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.cardDashRecyclerviewImage);
            title = (TextView) itemView.findViewById(R.id.cardDashRecyclerviewText);
            progressBar = (ProgressBar) itemView.findViewById(R.id.cardDashRecyclerviewProgress);
        }
    }

}
