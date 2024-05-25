package ru.mirea.kleninaa.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private EditText etName, etEmail;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        btnSave = view.findViewById(R.id.btn_save);
        sharedPreferences = getActivity().getSharedPreferences("ProfilePrefs", MODE_PRIVATE);

        loadProfile();

        btnSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void loadProfile() {
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");

        etName.setText(name);
        etEmail.setText(email);
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etName.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.apply();
    }
}