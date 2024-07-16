package com.example.pickcourt.Utilities;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalManager {
    private static volatile SignalManager instance = null;
    private Context context;

    private SignalManager(Context context) {
        this.context = context;
    }
    public static SignalManager init(Context context){
        if(instance == null){
            synchronized (SignalManager.class){
                if(instance == null){
                    instance = new SignalManager(context);
                }
            }
        }
        return getInstance();
    }
    public static SignalManager getInstance() {
        return instance;
    }


    public void toast(String message){
        Toast
                .makeText(context, message, Toast.LENGTH_SHORT)
                .show();
    }






}
