package ru.mirea.kleninaa.lesson_6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etGroup, etNumber, etMovie;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etGroup = findViewById(R.id.etGroup);
        etNumber = findViewById(R.id.etNumber);
        etMovie = findViewById(R.id.etMovie);
        btnSave = findViewById(R.id.btnSave);

        sharedPreferences = getSharedPreferences("mirea_settings", MODE_PRIVATE);

        loadPreferences();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GROUP", etGroup.getText().toString());
        editor.putInt("NUMBER", Integer.parseInt(etNumber.getText().toString()));
        editor.putString("MOVIE", etMovie.getText().toString());
        editor.apply();
    }

    private void loadPreferences() {
        String group = sharedPreferences.getString("GROUP", "");
        int number = sharedPreferences.getInt("NUMBER", 0);
        String movie = sharedPreferences.getString("MOVIE", "");

        etGroup.setText(group);
        etNumber.setText(String.valueOf(number));
        etMovie.setText(movie);
    }
}