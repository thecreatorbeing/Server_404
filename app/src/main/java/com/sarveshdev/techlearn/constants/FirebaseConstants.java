package com.sarveshdev.techlearn.constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConstants {
    public static final String DATABASE_URL = "https://tech-learn-ef2af-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final DatabaseReference QUERY_HOMEPAGE_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("homepage");
    public static final DatabaseReference QUERY_TIKTOK_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("tiktok");
    public static final DatabaseReference QUERY_VISITOR_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("visitors");

    public static final DatabaseReference QUERY_DISCUSSION_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("discussion");
    public static final DatabaseReference COMMENT_SECTION_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("comment_sections");

    public static final DatabaseReference ATTENDANCE_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("attendance");
    public static final DatabaseReference ATTENDANCE_RANDOM_NUMBER_NODE = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("publicNum");

}
