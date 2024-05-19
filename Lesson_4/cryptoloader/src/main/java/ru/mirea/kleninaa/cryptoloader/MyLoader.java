package ru.mirea.kleninaa.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class MyLoader extends AsyncTaskLoader<String> {

    private String firstName;

    public static final String ARG_WORD = "word";

    public MyLoader(@NonNull Context context, @Nullable Bundle args) {
        super(context);
        if (args != null)
            firstName = args.getString(ARG_WORD);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        SystemClock.sleep(5000);
        return firstName;
    }
}