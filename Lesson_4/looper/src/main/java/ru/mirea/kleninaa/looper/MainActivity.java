package ru.mirea.kleninaa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.kleninaa.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;
    private MyLooper my_looper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler =	new	Handler(Looper.getMainLooper())	{
            @Override
            public void handleMessage(Message msg) {
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));
            }
        };
        my_looper = new MyLooper(mainThreadHandler);
        my_looper.start();
    }

    public void OnSendMessageButtonClicked(View v) {
        Message msg	= Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("Работа", binding.workPlace.getText().toString());
        bundle.putInt("Возраст", Integer.parseUnsignedInt(binding.age.getText().toString()));
        msg.setData(bundle);
        my_looper.mHandler.sendMessage(msg);
    }
}