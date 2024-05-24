package ru.mirea.kleninaa.notebook;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private EditText etFileName, etQuote;
    private Button btnSave, btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileName = findViewById(R.id.etFileName);
        etQuote = findViewById(R.id.etQuote);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);

        btnSave.setOnClickListener(v -> saveToFile());
        btnLoad.setOnClickListener(v -> loadFromFile());

        checkPermission();
    }

    private void saveToFile() {
        String fileName = etFileName.getText().toString();
        String quote = etQuote.getText().toString();

        if (fileName.isEmpty() || quote.isEmpty()) {
            Toast.makeText(this, "Название файла и цитата не должны быть пустыми", Toast.LENGTH_SHORT).show();
            return;
        }

        // Получаем директорию внутреннего хранилища приложения
        File directory = new File(getFilesDir(), "MyFiles");

        // Если директория не существует, создаем её
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName + ".txt");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(quote.getBytes(StandardCharsets.UTF_8));
            fos.close();
            Toast.makeText(this, "Данные сохранены в файл", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("InternalStorage", "Error writing to file", e);
            Toast.makeText(this, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromFile() {
        if (!isExternalStorageReadable()) {
            Toast.makeText(this, "Внешнее хранилище недоступно", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = etFileName.getText().toString();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Название файла не должно быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName + ".txt");

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }

            etQuote.setText(stringBuilder.toString().trim());
            Toast.makeText(this, "Данные загружены из файла", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w("ExternalStorage", "Error reading " + file, e);
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}