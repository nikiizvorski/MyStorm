package com.androidchill.niki.mystorm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidchill.niki.mystorm.Model.CurrentWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private CurrentWeather mCurrentWeather;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String api_KEY = "d4d00719a0e6d725a6fe76692af3c0b8";
        double latitude = 37.8267;
        double longitude = -122.423;

        //hide action bar
        getSupportActionBar().hide();

        String forecastURL = "https://api.forecast.io/forecast/" + api_KEY + "/37.8267,-122.423";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
            JSONObject forecast = new JSONObject(jsonData);
            String timezone = forecast.getString("timezone");
            Log.i(TAG, "From JSON:" + timezone);

            JSONObject currently = forecast.getJSONObject("currently");
            CurrentWeather currentWeather = new CurrentWeather();
            currentWeather.setmHumidity(currently.getDouble("humidity"));
            currentWeather.setmTime(currently.getLong("time"));
            currentWeather.setmIcon(currently.getString("icon"));
            currentWeather.setmPrecipChance(currently.getDouble("precipProbability"));
            currentWeather.setmSummary(currently.getString("summary"));
            currentWeather.setmTemperature(currently.getDouble("temperature"));
            currentWeather.setmTimeZone(timezone);

            Log.d(TAG, currentWeather.getFormattedTime());

            return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager(), "error_dialog");
    }
}
