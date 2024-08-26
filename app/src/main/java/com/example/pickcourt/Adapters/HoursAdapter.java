package com.example.pickcourt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.R;

import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_HOUR = 1;

    private List<String> hours;
    private int selectedPosition = -1;

    public HoursAdapter(List<String> hours) {
        this.hours = hours;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TITLE : VIEW_TYPE_HOUR;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_TITLE) {
            View view = inflater.inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_hour, parent, false);
            return new HourViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_TITLE) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.titleText.setText(hours.get(position));
        } else {
            HourViewHolder hourViewHolder = (HourViewHolder) holder;
            String hour = hours.get(position);
            hourViewHolder.hourText.setText(hour);

            // Change background and text color based on selection
            if (selectedPosition == position) {
                hourViewHolder.hourText.setBackgroundResource(R.drawable.selected_hour_background);
                hourViewHolder.hourText.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
            } else {
                hourViewHolder.hourText.setBackgroundResource(R.drawable.regular_button);
                hourViewHolder.hourText.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
            }

            hourViewHolder.itemView.setOnClickListener(v -> {
                selectedPosition = hourViewHolder.getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    public String getSelectedHour() {
        if (selectedPosition > 0) { // Ensure it's not the title
            return hours.get(selectedPosition);
        }
        return null; // No hour selected
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;

        TitleViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
        }
    }

    static class HourViewHolder extends RecyclerView.ViewHolder {
        TextView hourText;

        HourViewHolder(View itemView) {
            super(itemView);
            hourText = itemView.findViewById(R.id.hour_text);
        }
    }
}