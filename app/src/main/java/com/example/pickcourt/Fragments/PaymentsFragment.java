package com.example.pickcourt.Fragments;

import android.app.AlertDialog;

import android.os.Bundle;


import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.braintreepayments.cardform.view.CardForm;
import com.example.pickcourt.Adapters.PaymentAdapter;
import com.example.pickcourt.DataBaseManager;
import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.Models.Payment;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.SignalManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PaymentsFragment extends Fragment {

    private RecyclerView payments_LST;
    private AppCompatImageButton payments_BTN_add;
    private PaymentAdapter paymentAdapter;
    private AppCompatImageView toggle_sidebar_button;
    private OpenDrawerCallback openDrawerCallback;
    public PaymentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payments, container, false);
        findViews(v);
        initViews();
        return v;
    }

    private void findViews(View v){
        payments_LST = v.findViewById(R.id.payments_LST);
        payments_BTN_add = v.findViewById(R.id.payments_BTN_add);
        toggle_sidebar_button = v.findViewById(R.id.toggle_sidebar_button);
    }

    public PaymentsFragment setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
        return this;
    }

    private void initViews() {
        payments_BTN_add.setOnClickListener((v) -> addPayment());
        toggle_sidebar_button.setOnClickListener((v) -> openDrawerCallback.openDrawer());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        payments_LST.setLayoutManager(linearLayoutManager);

        DataBaseManager.getInstance().setPaymentCallback(payments -> {
            paymentAdapter = new PaymentAdapter(payments);
            payments_LST.setAdapter(paymentAdapter);
            paymentAdapter.setOnPaymentDeleteLister(this::deletePayment);
            paymentAdapter.setOnDefaultCardChangeListener(this::changeDefaultPayment);
        });

        DataBaseManager.getInstance().fetchCreditCards();
    }

    private void changeDefaultPayment(Payment payment) {
        if(payment.getDefaultCard()) return;

        new AlertDialog.Builder(requireContext())
                .setTitle("Change Default Card")
                .setMessage("Do you want to set this card as your default payment method?")
                .setPositiveButton("Yes", (dialog, which) -> DataBaseManager.getInstance().setDefaultCard(payment))
                .setNegativeButton("No", null)
                .show();


    }

    private void deletePayment(Payment payment) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Payment")
                .setMessage("Are you sure you want to delete this payment method?")
                .setPositiveButton("Yes", (dialog, which) -> DataBaseManager.getInstance().deleteCreditCard(payment))
                .setNegativeButton("No", null)
                .show();
    }


    private void addPayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_add_card, null);

        CardForm cardForm = dialogView.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .mobileNumberRequired(false)
                .setup(requireActivity());

        TextInputEditText idEditText = dialogView.findViewById(R.id.id_edit_text);

        builder.setView(dialogView)
                .setTitle("Add New Credit Card")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String id = idEditText.getText().toString().trim();
                String cardNumber = cardForm.getCardNumber();
                if (cardForm.isValid() && isValidIsraeliID(id)) {
                    if (isCardAlreadyAdded(cardNumber)) {
                        SignalManager.getInstance().toast("This card is already added");
                    } else {
                        String expirationDate = cardForm.getExpirationMonth() + "/" + cardForm.getExpirationYear();
                        Payment payment = new Payment(id, cardNumber, expirationDate, cardForm.getCvv(), false);
                        DataBaseManager.getInstance().addCreditCard(payment);
                        dialog.dismiss();
                    }
                } else {
                    if (!cardForm.isValid())
                        SignalManager.getInstance().toast("Please complete all card fields");
                    else if (!isValidIsraeliID(id))
                        SignalManager.getInstance().toast("Please enter a valid ID");
                }
            });
        });

        dialog.show();
    }

    private boolean isCardAlreadyAdded(String cardNumber) {
        if (paymentAdapter != null && paymentAdapter.getPayments() != null) {
            for (Payment payment : paymentAdapter.getPayments())
                if (payment.getCardNumber().equals(cardNumber))
                    return true;
        }
        return false;
    }

    private boolean isValidIsraeliID(String id) {
        id = id.trim();
        if (id.length() > 9 || id.length() < 5 || !id.matches("\\d+")) return false;
        id = String.format("%09d", Long.parseLong(id));
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(id.charAt(i));
            int step = digit * ((i % 2) + 1);
            sum += step > 9 ? step - 9 : step;
        }
        return sum % 10 == 0;
    }
}