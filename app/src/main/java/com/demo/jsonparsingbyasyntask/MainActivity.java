package com.demo.jsonparsingbyasyntask;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView companyNameTV, mobileNameTV, screenSizeTV, batteryTV, ramTV;
    private String api = "https://androidtutorialpoint.com/api/MobileJSONArray.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        companyNameTV = findViewById(R.id.companyNameTV);
        mobileNameTV = findViewById(R.id.mobileNameTV);
        screenSizeTV = findViewById(R.id.screenSizeTV);
        batteryTV = findViewById(R.id.batteryTV);
        ramTV = findViewById(R.id.ramTV);

        new MobileTask().execute(api);

    }

    public class MobileTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String s = strings[0];

            try {
                URL url = new URL(s);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                if ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                companyNameTV.setText(jsonObject.get("companyName").toString());
                mobileNameTV.setText(jsonObject.get("name").toString());
                screenSizeTV.setText(jsonObject.get("screenSize").toString());
                batteryTV.setText(jsonObject.get("battery").toString());
                ramTV.setText(jsonObject.get("ram").toString());

                new imageDownload().execute(jsonObject.get("url").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    public class imageDownload extends AsyncTask<String, Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }

}
