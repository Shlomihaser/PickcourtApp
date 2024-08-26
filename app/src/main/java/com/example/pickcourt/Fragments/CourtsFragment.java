package com.example.pickcourt.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pickcourt.Interfaces.OnCourtClickListener;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pickcourt.Adapters.CourtAdapter;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.R;
import java.util.ArrayList;

public class CourtsFragment extends Fragment  {
    private RecyclerView home_LST_courts;
    private CourtAdapter courtAdapter;
    private OnCourtClickListener courtSelectedListener;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_courts, container, false);
        findViews(v);
        initViews();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnCourtClickListener)
            courtSelectedListener = (OnCourtClickListener) getParentFragment();
        else if (context instanceof OnCourtClickListener)
            courtSelectedListener = (OnCourtClickListener) context;
         else
            throw new RuntimeException(context.toString() + " must implement OnCourtSelectedListener");
    }

    private void findViews(View v) {
        home_LST_courts = v.findViewById(R.id.home_LST_courts);
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_courts.setLayoutManager(linearLayoutManager);

        DataBaseManager.getInstance().setCourtsCallback(courts -> {
            courtAdapter = new CourtAdapter(courts);
            courtAdapter.setCourtClickListener(courtSelectedListener);

            home_LST_courts.setAdapter(courtAdapter);
            DataBaseManager.getInstance().setIsFavoriteCallback((courtName, isFavorite) -> {
                for (Court court : courts) {
                    if (court.getName().equals(courtName)) {
                        court.setFavorite(isFavorite);
                        int position = courts.indexOf(court);
                        courtAdapter.notifyItemChanged(position);
                        break;
                    }
                }
            });
        });
    }

    public void updateCourts(ArrayList<Court> courts) {
        if(courts == null || courtAdapter == null) return;
        courtAdapter.updateCourts(courts);
    }
}
