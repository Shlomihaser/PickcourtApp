package com.example.pickcourt;

import android.app.Application;

import com.example.pickcourt.Utilities.ImageLoader;
import com.example.pickcourt.Utilities.SignalManager;

public class App extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        SignalManager.init(this);
        DataBaseManager.init(this);
        ImageLoader.init(this);
    }
}
