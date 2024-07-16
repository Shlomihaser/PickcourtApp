package com.example.pickcourt;

import android.app.Application;

import com.example.pickcourt.Utilities.SignalManager;

public class App extends Application{

    public void onCreate(){
        super.onCreate();
        SignalManager.init(this);

    }
}
