package com.sarveshdev.techlearn.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseApi {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getSignedInUser(){
        return firebaseAuth.getCurrentUser();
    }

}
