package com.example.pickcourt.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.Adapters.HoursAdapter;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Models.Reservation;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.NonScrollableGridLayoutManager;
import com.example.pickcourt.Utilities.SignalManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingFragment extends Fragment {

    private Button buttonDatePicker;
    private EditText inputNumPlayers;
    private Button submitButton;
    private RecyclerView recyclerViewHours;
    private HoursAdapter hoursAdapter;
    private ArrayList<String> availableHours;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        findViews(view);
        initViews();
        setDefaultValues();
        setupRecyclerView();
        return view;
    }

    private void findViews(View view) {
        buttonDatePicker = view.findViewById(R.id.button_date_picker);
        inputNumPlayers = view.findViewById(R.id.input_num_players);
        submitButton = view.findViewById(R.id.submit_button);
        recyclerViewHours = view.findViewById(R.id.recycler_view_hours);
    }

    private void initViews() {
        buttonDatePicker.setOnClickListener(v -> showDatePickerDialog());
        submitButton.setOnClickListener(v -> submitForm());
    }

    private void setDefaultValues() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String todayDate = String.format("%d/%d", day, month, year);
        this.selectedDate = todayDate;
        buttonDatePicker.setText(todayDate);
        inputNumPlayers.setText("2");
    }

    private void setupRecyclerView() {
        availableHours = getArguments().getStringArrayList("availableHours");

        // Add a placeholder for the title in the list
        availableHours.add(0, "Choose Hour");

        hoursAdapter = new HoursAdapter(availableHours);
        NonScrollableGridLayoutManager layoutManager = new NonScrollableGridLayoutManager(getContext(), 5);

        // Set SpanSizeLookup to make the title span 2 columns
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1; // Title takes 2 columns, others take 1
            }
        });

        recyclerViewHours.setLayoutManager(layoutManager);
        recyclerViewHours.setAdapter(hoursAdapter);

        // Apply item decoration for spacing
        recyclerViewHours.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int space = 8; // Space in pixels
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;

                // Add top margin only for the first row to avoid double space between items
                if (parent.getChildAdapterPosition(view) < 5) { // Assuming 5 columns
                    outRect.top = space;
                }
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            String selectedDate = String.format("%d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
            this.selectedDate = selectedDate;
            buttonDatePicker.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void submitForm() {
        String numPlayersStr = inputNumPlayers.getText().toString();
        if (numPlayersStr.isEmpty()) {
            SignalManager.getInstance().toast("Please enter the number of players");
            return;
        }
        int numPlayers = Integer.parseInt(numPlayersStr);
        if (numPlayers < 2 || numPlayers > 5)
        {
            SignalManager.getInstance().toast("Number of players must be between 2 and 5");
            return;
        }

        String selectedHour = hoursAdapter.getSelectedHour();
        if (selectedHour == null) {
            SignalManager.getInstance().toast("Please select an hour");
            return;
        }
        Bundle args = getArguments();
        Reservation newReservation = new Reservation(args.getString("courtName"),selectedHour,selectedDate,numPlayers,args.getString("courtImage"));
        DataBaseManager.getInstance().addReservation(newReservation);
        SignalManager.getInstance().toast("Court reserved !");
        ((CourtInfoFragment) getParentFragment()).navigateToHomeFragment();
    }
}