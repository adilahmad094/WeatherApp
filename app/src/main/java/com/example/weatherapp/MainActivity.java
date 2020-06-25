package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView humidity, min, max, wind, cityTV, descTV, tempTV;
    EditText search;
    ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidity = findViewById(R.id.humidityTextView);
        min = findViewById(R.id.MinTextView);
        max = findViewById(R.id.MaxTextView);
        wind = findViewById(R.id.WindTextView);
        search = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchImageView);
        cityTV = findViewById(R.id.cityTextView);
        descTV = findViewById(R.id.descTextView);
        tempTV = findViewById(R.id.tempTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
//                api_key(String.valueOf(search.getText()));
                String s = String.valueOf(search.getText());
                api_key(s);
            }
        });
    }

    private void api_key(final String City) {
        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=f4829e5ac85ea1dde5ce4aa5aabc4aa5&units=metric")
                .get()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response= client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData= response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray array = json.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        String description=object.getString("description");

                        JSONObject temp1 = json.getJSONObject("main");
                        Double Temperature = temp1.getDouble("temp");
                        Double minTemp = temp1.getDouble("temp_min");
                        Double maxTemp = temp1.getDouble("temp_max");
                        Double humid = temp1.getDouble("humidity");

                        JSONObject temp2 = json.getJSONObject("wind");
                        Double windspeed = temp2.getDouble("speed");


                        setText(cityTV, City.toUpperCase());

                        String temps = Math.round(Temperature)+"°";
                        setText(tempTV, temps);

                        setText(descTV,description);

                        temps = Math.round(humid)+"%";
                        setText(humidity, temps);

                        temps = Math.round(windspeed)+" mph";
                        setText(wind, temps);

                        temps = Math.round(minTemp)+"°";
                        setText(min, temps);

                        temps = Math.round(maxTemp)+"°";
                        setText(max, temps);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    private void setText(final TextView text, final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
}