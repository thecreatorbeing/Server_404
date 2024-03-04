package com.sarveshdev.techlearn.ui.attendance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarveshdev.techlearn.R;

public class AttendanceFragment extends Fragment {


    public AttendanceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_attendance, container, false);



        return root;
    }
}