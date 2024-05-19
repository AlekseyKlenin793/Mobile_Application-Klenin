package ru.mirea.kleninaa.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

import ru.mirea.kleninaa.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread mainThread = Thread.currentThread();
        binding.textView.setText("Имя текущего потока: " + mainThread.getName());

        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-04-22, НОМЕР ПО СПИСКУ: 13, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: -");
        binding.textView.append("\nНовое имя потока: " + mainThread.getName());

        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalPairsStr = binding.totalPairsEditText.getText().toString();
                String studyDaysStr = binding.studyDaysEditText.getText().toString();

                if (totalPairsStr.isEmpty() || studyDaysStr.isEmpty()) {
                    binding.textView.append("\nВведите все данные.");
                    return;
                }

                int totalPairs = Integer.parseInt(totalPairsStr);
                int studyDays = Integer.parseInt(studyDaysStr);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %s ", numberThread, "БСБО-04-22", "13"));

                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        float averagePairsPerDay = calculateAveragePairsPerDay(totalPairs, studyDays);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textView.append(String.format("\nСреднее количество пар в день: %.2f", averagePairsPerDay));
                                Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private float calculateAveragePairsPerDay(int totalPairs, int studyDays) {
        if (studyDays == 0) {
            return 0;
        }
        return (float) totalPairs / studyDays;
    }
}