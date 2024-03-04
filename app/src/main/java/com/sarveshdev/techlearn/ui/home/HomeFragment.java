package com.sarveshdev.techlearn.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarveshdev.techlearn.constants.FirebaseConstants;
import com.sarveshdev.techlearn.databinding.FragmentHomeBinding;
import com.sarveshdev.techlearn.recyclerview_api.news.HomeRecyclerAdapter;
import com.sarveshdev.techlearn.recyclerview_api.news.HomeRecyclerModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView homeRecyclerView;
    FirebaseRecyclerOptions<HomeRecyclerModel> options;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        try {
            homeRecyclerView = binding.fragHomeRecyclerView;
            options = new FirebaseRecyclerOptions.Builder<HomeRecyclerModel>()
                    .setQuery( FirebaseConstants.QUERY_HOMEPAGE_NODE, HomeRecyclerModel.class)
                    .build();
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            homeRecyclerAdapter = new HomeRecyclerAdapter(options);
            homeRecyclerView.setAdapter(homeRecyclerAdapter);
            homeRecyclerAdapter.startListening();
        } catch(Exception e){
            Toast.makeText(getContext(), "Error Occurred on main RecyclerView!", Toast.LENGTH_SHORT).show();
        } //this is in-built error causing app-crashing!

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        homeRecyclerAdapter.stopListening();
    }
}