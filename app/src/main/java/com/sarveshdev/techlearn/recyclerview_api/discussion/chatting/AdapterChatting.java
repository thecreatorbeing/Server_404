package com.sarveshdev.techlearn.recyclerview_api.discussion.chatting;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sarveshdev.techlearn.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatting extends FirebaseRecyclerAdapter<ModelChatting, AdapterChatting.ChattingViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterChatting(@NonNull FirebaseRecyclerOptions<ModelChatting> options) {
        super(options);
    }//don't touch this!

    @NonNull
    @Override
    public ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_chatting, parent,  false);

        return new ChattingViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChattingViewHolder holder, int position, @NonNull ModelChatting model) {

        holder.textUserName.setText(model.getUserName());

        holder.textComment.setText(model.getComment());


        Glide.with(holder.circleImageView.getContext())
                .load(model.getUserIconUrl())
                .placeholder(R.drawable.logo)
                .into(holder.circleImageView);
        Log.d("sarvesh_log", "model loaded!");
    }

    public class ChattingViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView textUserName, textComment;

        public ChattingViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.fragChatImgUser);
            textUserName = itemView.findViewById(R.id.fragChatTextUsername);
            textComment = itemView.findViewById(R.id.fragChatTextComment);
        }
    }
}
