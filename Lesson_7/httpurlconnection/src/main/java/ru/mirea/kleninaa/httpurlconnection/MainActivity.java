package ru.mirea.kleninaa.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.kleninaa.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (connectivityManager != null) {
                    networkinfo = connectivityManager.getActiveNetworkInfo();
                }
                if (networkinfo != null && networkinfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textViewIp.setText("Загружаем...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            binding.textViewIp.setText(result);
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(TAG, "Response: " + responseJson);
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");

                binding.textViewIp.setText("IP: " + ip);
                binding.textViewCity.setText("City: " + city);
                binding.textViewRegion.setText("Region: " + region);

                // Получение координат для запроса погоды
                String[] loc = responseJson.getString("loc").split(",");
                String latitude = loc[0];
                String longitude = loc[1];

                // Запрос погоды
                new DownloadWeatherTask().execute("https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true");

            } catch (JSONException e) {
                e.printStackTrace();
                binding.textViewIp.setText("JSON Parsing Error");
            }
            super.onPostExecute(result);
        }
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textViewWeather.setText("Загружаем погоду...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadWeatherInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            binding.textViewWeather.setText(result);
            Log.d(TAG, result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(TAG, "Weather Response: " + responseJson);
                String weather = responseJson.getJSONObject("current_weather").getString("temperature");

                binding.textViewWeather.setText("Weather: " + weather + "°C");
            } catch (JSONException e) {
                e.printStackTrace();
                binding.textViewWeather.setText("Weather JSON Parsing Error");
            }
            super.onPostExecute(result);
        }
    }

    private String downloadWeatherInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}
