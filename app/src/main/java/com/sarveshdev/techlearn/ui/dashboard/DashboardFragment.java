package com.sarveshdev.techlearn.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarveshdev.techlearn.R;
import com.sarveshdev.techlearn.ViewVideoActivity;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.constants.IntentConstants;
import com.sarveshdev.techlearn.databinding.FragmentDashboardBinding;
import com.sarveshdev.techlearn.recyclerview_api.RecyclerItemClickListener;
import com.sarveshdev.techlearn.recyclerview_api.videos.VideoModel;
import com.sarveshdev.techlearn.recyclerview_api.videos.VideoRecyclerAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    RecyclerView recyclerView;
    VideoRecyclerAdapter videoAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseRecyclerOptions<VideoModel> options;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        try {
            recyclerView = binding.fragDashRecyclerView;
            options = new FirebaseRecyclerOptions.Builder<VideoModel>()
                    .setQuery(FirebaseConstants.QUERY_TIKTOK_NODE, VideoModel.class)
                    .build();
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(staggeredManager);
            videoAdapter = new VideoRecyclerAdapter(options);
            recyclerView.setAdapter(videoAdapter);
            videoAdapter.startListening();

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getContext(), ViewVideoActivity.class);
                    intent.putExtra(IntentConstants.TIKTOK_TITLE,
                            ((TextView)view.findViewById(R.id.cardDashRecyclerviewText)).getText());
                    startActivity(intent);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }));

        } catch(Exception e){
            Toast.makeText(getContext(), "error occured :"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        videoAdapter.stopListening();
    }
}