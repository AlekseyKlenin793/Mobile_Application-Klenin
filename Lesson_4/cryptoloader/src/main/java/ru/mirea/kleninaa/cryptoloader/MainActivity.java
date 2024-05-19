package ru.mirea.kleninaa.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import android.app.LoaderManager.LoaderCallbacks;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.security.InvalidParameterException;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int LOADER_ID = 123;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secretKey = CryptoUtils.generateKey();

        Bundle bundle = new Bundle();
        LoaderManager.getInstance(this).initLoader(LOADER_ID, bundle, this);

        findViewById(R.id.buttonLoad).setOnClickListener(view -> {
            String inputText = ((EditText)findViewById(R.id.editText)).getText().toString();
            String encryptedText = CryptoUtils.encryptMsg(inputText, secretKey);
            Bundle args = new Bundle();
            args.putString(MyLoader.ARG_WORD, encryptedText);
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (loader.getId() == LOADER_ID) {
            String decryptedText = CryptoUtils.decryptMsg(data, secretKey);
            Toast.makeText(this, "Decrypted: " + decryptedText, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //
    }
}