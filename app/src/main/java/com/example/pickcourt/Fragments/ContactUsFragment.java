package com.example.pickcourt.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.pickcourt.Interfaces.OpenDrawerCallback;
import com.example.pickcourt.R;
import com.example.pickcourt.Utilities.SignalManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactUsFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextMessage;
    private Button buttonSubmit;
    private OpenDrawerCallback openDrawerCallback;
    private AppCompatImageButton toggle_sidebar_button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_contactus, container, false);
        findViews(v);
        initViews();
        return v;
    }



    private void findViews(View v) {
        editTextName = v.findViewById(R.id.editTextName);
        editTextEmail = v.findViewById(R.id.editTextEmail);
        editTextMessage = v.findViewById(R.id.editTextMessage);
        buttonSubmit = v.findViewById(R.id.buttonSubmit);
        toggle_sidebar_button = v.findViewById(R.id.toggle_sidebar_button);
    }

    private void initViews() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && !currentUser.isAnonymous()) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            if (name != null && !name.isEmpty()) editTextName.setText(name);
            if (email != null && !email.isEmpty()) editTextEmail.setText(email);
        }

        buttonSubmit.setOnClickListener(v -> submitForm());
        toggle_sidebar_button.setOnClickListener(v -> openDrawerCallback.openDrawer());
    }

    private void submitForm() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            SignalManager.getInstance().toast("Please fill all fields");
            return;
        }

        // TODO: Implement your form submission logic here
        // For example, you could send this data to your server or Firebase
        // Create the email intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hasshlomi21@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Form Submission from " + name);
        intent.putExtra(Intent.EXTRA_TEXT,
                "Name: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Message: " + message);

        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
            SignalManager.getInstance().toast("Choose an email client to send the message");
            editTextMessage.setText(""); // Clear only the message field
        } catch (android.content.ActivityNotFoundException ex) {
            SignalManager.getInstance().toast("There are no email clients installed on your device.");
        }
        editTextMessage.setText("");
    }

    public void setOpenDrawerCallback(OpenDrawerCallback openDrawerCallback) {
        this.openDrawerCallback = openDrawerCallback;
    }
}

