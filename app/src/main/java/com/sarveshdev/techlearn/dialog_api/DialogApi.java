package com.sarveshdev.techlearn.dialog_api;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.sarveshdev.techlearn.R;


public class DialogApi {

    /**set onCancellable() while using and call show() externally!*/
    public static AlertDialog createNetworkErrorDialog(Context context){
         return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo)
                .setTitle("Network not available!")
                .setMessage("Please connect your phone to network using:\n" +
                        "1}. mobile data network\n" +
                        "2} Wi-Fi")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }//createDialog() ended!


}//DialogApi class ended!