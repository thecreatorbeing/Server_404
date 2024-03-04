package com.sarveshdev.techlearn.recyclerview_api.news;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
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


public class HomeRecyclerAdapter extends FirebaseRecyclerAdapter<HomeRecyclerModel, HomeRecyclerAdapter.mViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeRecyclerAdapter(@NonNull FirebaseRecyclerOptions<HomeRecyclerModel> options) {
        super(options);
    }//don't touch this!

    @Override
    protected void onBindViewHolder(@NonNull mViewHolder holder, int position, @NonNull HomeRecyclerModel model) {
        holder.mProgressBar.setIndeterminate(true);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) holder.mImageView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //3:4 aspect ratio
        holder.mImageView.getLayoutParams().height = (int) ((3f/4) * displaymetrics.widthPixels);

        holder.mTitle.setText(model.getTitle());
        holder.mDate.setText(model.getDate());
        Glide.with(holder.mImageView.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.logo)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(holder.mImageView.getContext(), "failed to load image!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.mImageView);
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card, parent, false);
        return new mViewHolder(view);
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView; TextView mTitle, mDate;
        ProgressBar mProgressBar;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.fragHomeImgPost);
            mTitle = itemView.findViewById(R.id.fragHomeTextTitle);
            mDate = itemView.findViewById(R.id.fragHomeTextDate);
            mProgressBar = itemView.findViewById(R.id.fragHomeProgressBar);
        }
    }
}//HomeRecyclerAdapter class ended!