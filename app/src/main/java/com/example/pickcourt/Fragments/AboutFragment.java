package com.example.pickcourt.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.R;

public class AboutFragment extends Fragment {

    private AppCompatImageView toggle_sidebar_button;
    private OpenDrawerCallback openDrawerCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_about, container, false);
        findViews(v);
        initViews();
        return v;
    }

    public void setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
    }

    private void findViews(View v) {
        toggle_sidebar_button = v.findViewById(R.id.toggle_sidebar_button);
    }

    private void initViews() {
        toggle_sidebar_button.setOnClickListener((v) -> openDrawerCallback.openDrawer());
    }
}
