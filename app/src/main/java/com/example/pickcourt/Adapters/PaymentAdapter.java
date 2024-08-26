package com.example.pickcourt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickcourt.Interfaces.OnDefaultCardChangeListener;
import com.example.pickcourt.Interfaces.PaymentDeleteListener;
import com.example.pickcourt.Models.Payment;
import com.example.pickcourt.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private final ArrayList<Payment> payments;
    private PaymentDeleteListener deleteListener;
    private OnDefaultCardChangeListener defaultCardChangeListener;

    public PaymentAdapter(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public void setOnPaymentDeleteLister(PaymentDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnDefaultCardChangeListener(OnDefaultCardChangeListener listener) {
        this.defaultCardChangeListener = listener;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.PaymentViewHolder holder, int position) {
        Payment payment = getItem(position);
        Context context = holder.itemView.getContext();

        String last4digits = payment.getCardNumber().substring(payment.getCardNumber().length() - 4);
        String encryptedCard = "**** **** **** " + last4digits;
        holder.card_number.setText(encryptedCard);

        String cardNum = payment.getCardNumber();
        if (cardNum.startsWith("4")) holder.card_IMG.setImageResource(R.drawable.ic_visa);
        else if (cardNum.startsWith("5")) holder.card_IMG.setImageResource(R.drawable.ic_mastercard);
        else if (cardNum.startsWith("34") || cardNum.startsWith("37") || cardNum.startsWith("27")) holder.card_IMG.setImageResource(R.drawable.ic_amex);
        else if (cardNum.startsWith("3")) holder.card_IMG.setImageResource(R.drawable.ic_diners);
        else if (cardNum.startsWith("7")) holder.card_IMG.setImageResource(R.drawable.ic_isracard);
        else holder.card_IMG.setImageResource(R.drawable.ic_credit_card);

        holder.delete_button.setOnClickListener(v -> deleteListener.onPaymentDelete(payment));
        holder.payment_item.setOnClickListener(v -> defaultCardChangeListener.onDefaultCardChanged(payment));


        if (payment.getDefaultCard()) {

            holder.payment_item.setStrokeColor(ContextCompat.getColor(context, R.color.primary_blue));
            holder.payment_item.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.default_card_stroke_width));
            holder.payment_item.setCardElevation(30);
        } else {
            holder.payment_item.setCardElevation(4);
            holder.payment_item.setPadding(0,0,0,0);
            holder.payment_item.setStrokeColor(ContextCompat.getColor(context, R.color.card_stroke_color));
            holder.payment_item.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.card_stroke_width));
        }
    }

    private String maskCardNumber(String cardNumber) {
        int visibleDigits = 4;
        int totalLength = cardNumber.length();
        int maskedLength = totalLength - visibleDigits;

        StringBuilder maskedNumber = new StringBuilder();

        for (int i = 0; i < maskedLength; i++) {
            if (i > 0 && i % 4 == 0) maskedNumber.append(" ");
            maskedNumber.append("*");
        }
        if (maskedLength % 4 != 0) maskedNumber.append(" ");
        maskedNumber.append(cardNumber.substring(totalLength - visibleDigits));

        return maskedNumber.toString();
    }

    @Override
    public int getItemCount() {
        return payments == null ? 0 : payments.size();
    }

    private Payment getItem(int position) {
        return payments.get(position);
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView payment_item;
        private final ImageButton delete_button;
        private final TextView card_number;
        private final ImageView card_IMG;
      //  private final TextView default_label;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            delete_button = itemView.findViewById(R.id.delete_button);
            payment_item = itemView.findViewById(R.id.payment_item);
            card_number = itemView.findViewById(R.id.card_number);
            card_IMG = itemView.findViewById(R.id.card_IMG);
          //   default_label = itemView.findViewById(R.id.default_label);
        }
    }
}