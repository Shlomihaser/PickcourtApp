package com.example.pickcourt.Utilities;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

public class NonScrollableGridLayoutManager extends GridLayoutManager {

    public NonScrollableGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollVertically() {
        return false; // Disable vertical scrolling
    }

    @Override
    public boolean canScrollHorizontally() {
        return false; // Disable horizontal scrolling if needed
    }
}