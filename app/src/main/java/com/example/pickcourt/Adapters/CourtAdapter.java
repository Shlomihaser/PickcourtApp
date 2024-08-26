package com.example.pickcourt.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Interfaces.OnCourtClickListener;
import com.example.pickcourt.Models.Court;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {
    private ArrayList<Court> courts;
    private OnCourtClickListener listener;

    public CourtAdapter(ArrayList<Court> courts){
        this.courts = courts;
    }



    public void updateCourts(ArrayList<Court> courts){
        this.courts.clear();
        this.courts.addAll(courts);
        notifyDataSetChanged();
    }

    public void setCourtClickListener(OnCourtClickListener courtClickListener) {
        this.listener = courtClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull CourtAdapter.CourtViewHolder holder, int position) {
        Court court = courts.get(position);
        holder.court_LBL_name.setText(court.getName());
        holder.court_LBL_indoorOutdoor.setText(court.isIndoor() ? "Indoor" : "Outdoor");
        holder.court_LBL_price.setText(String.format("%.2f₪", court.getPricePerHour()));
        holder.court_LBL_rating.setText("Rating: " + String.valueOf(court.getRating()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.isAnonymous())
            holder.court_FAB_favorite.setVisibility(View.INVISIBLE);

        updateFavoriteIcon(holder.court_FAB_favorite, court.isFavorite());
        DataBaseManager.getInstance().checkFavoriteStatus(court.getName());

        holder.court_FAB_favorite.setOnClickListener((v) -> {
            court.setFavorite(!court.isFavorite());
            DataBaseManager.getInstance().toggleFavorite(court.getName(),court.isFavorite());
            updateFavoriteIcon(holder.court_FAB_favorite,court.isFavorite());
        });

        holder.court_CARD.setOnClickListener(v -> listener.onCourtClick(court));

        if (!court.getImages().isEmpty())
            ImageLoader.getInstance().load(court.getImages().get(0),holder.court_IMG_photo);
    }


    private void updateFavoriteIcon(AppCompatImageButton fab, boolean isFavorite) {
        fab.setImageResource(isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
    }

    @NonNull
    @Override
    public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_court_item, parent, false);
        return new CourtAdapter.CourtViewHolder(view);
    }

    public static class CourtViewHolder extends RecyclerView.ViewHolder{
        private final CardView court_CARD;
        private final ImageView court_IMG_photo;
        private final TextView court_LBL_name;
        private final TextView court_LBL_indoorOutdoor;
        private final TextView court_LBL_price;
        private final AppCompatImageButton court_FAB_favorite;
        private final TextView court_LBL_rating;

        public CourtViewHolder(@NonNull View itemView){
            super(itemView);
            court_FAB_favorite = itemView.findViewById(R.id.court_FAB_favorite);
            court_CARD = itemView.findViewById(R.id.court_CARD);
            court_IMG_photo = itemView.findViewById(R.id.court_IMG_photo);
            court_LBL_name = itemView.findViewById(R.id.court_LBL_name);
            court_LBL_indoorOutdoor = itemView.findViewById(R.id.court_LBL_indoorOutdoor);
            court_LBL_price = itemView.findViewById(R.id.court_LBL_price);
            court_LBL_rating = itemView.findViewById(R.id.court_LBL_rating);
        }
    }
    @Override
    public int getItemCount() {
        return courts != null ? courts.size() : 0;
    }

}
