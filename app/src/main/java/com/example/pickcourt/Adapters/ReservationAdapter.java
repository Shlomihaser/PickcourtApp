package com.example.pickcourt.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Models.Reservation;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.ImageLoader;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private ArrayList<Reservation> reservations;

    public ReservationAdapter(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.reservation_LBL_courtName.setText(reservation.getCourtName());
        holder.reservation_LBL_hour.setText("Hour: " + reservation.getHour());
        holder.reservation_LBL_date.setText("Date: " + reservation.getDate());
        holder.reservation_LBL_numPlayers.setText("Players: " + reservation.getNumOfPlayers());
        ImageLoader.getInstance().load(reservation.getCourtImageUrl(),holder.reservation_IMG_photo);
        holder.reservation_BTN_cancel.setOnClickListener(v -> {
            cancelReservation(reservation);
        });
    }

    @Override
    public int getItemCount() {
        return reservations != null ? reservations.size() : 0;
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final CardView reservation_CARD;
        private final TextView reservation_LBL_courtName;
        private final TextView reservation_LBL_hour;
        private final TextView reservation_LBL_date;
        private final TextView reservation_LBL_numPlayers;
        private final MaterialButton reservation_BTN_cancel;
        private final ShapeableImageView reservation_IMG_photo;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            reservation_CARD = itemView.findViewById(R.id.reservation_CARD);
            reservation_LBL_courtName = itemView.findViewById(R.id.reservation_LBL_courtName);
            reservation_LBL_hour = itemView.findViewById(R.id.reservation_LBL_hour);
            reservation_LBL_date = itemView.findViewById(R.id.reservation_LBL_date);
            reservation_LBL_numPlayers = itemView.findViewById(R.id.reservation_LBL_numPlayers);
            reservation_BTN_cancel = itemView.findViewById(R.id.reservation_BTN_cancel);
            reservation_IMG_photo = itemView.findViewById(R.id.reservation_IMG_photo);
        }
    }

    private void cancelReservation(Reservation reservation) {
        DataBaseManager.getInstance().cancelReservation(reservation.getReservationId());
        reservations.remove(reservation);
        notifyDataSetChanged();
    }
}