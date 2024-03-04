package com.sarveshdev.techlearn.dataNetworkCheck_api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;

import com.sarveshdev.techlearn.constants.ApiConstants;


public class DataConnectionObserverAPI extends LiveData<ConnectionModel> {

        private Context context;

        public DataConnectionObserverAPI(Context context) {
            this.context = context;
        }

        @Override
        protected void onActive() {
            super.onActive();
            IntentFilter filter = new    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(networkReceiver, filter);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            context.unregisterReceiver(networkReceiver);
        }

        private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras()!=null) {
                    NetworkInfo activeNetwork = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if(isConnected) {
                        switch (activeNetwork.getType()){
                            case ConnectivityManager.TYPE_WIFI:
                                postValue(new ConnectionModel(ApiConstants.DataNetwork.FROM_WIFI,true));
                                break;
                            case ConnectivityManager.TYPE_MOBILE:
                                postValue(new ConnectionModel(ApiConstants.DataNetwork.FROM_MOBILE_DATA,true));
                                break;
                        }
                    } else {
                        postValue(new ConnectionModel(ApiConstants.DataNetwork.NOT_CONNECTED,false));
                    }
                }
            }
        };
}
