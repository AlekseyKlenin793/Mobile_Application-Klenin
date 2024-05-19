package ru.mirea.kleninaa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread{
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                final String workPlace = msg.getData().getString("Работа");
                final int age = msg.getData().getInt("Возраст");
                synchronized(this){
                    String result = "";
                    try {
                        wait(age * 1000);
                        result = String.format("Success for: %s", workPlace);
                    } catch (InterruptedException e) {
                        result = String.format("Interrupted for: %s", workPlace);
                    } finally {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        message.setData(bundle);
                        mainHandler.sendMessage(message);
                    }
                }
            }
        };
        Looper.loop();
    }
}