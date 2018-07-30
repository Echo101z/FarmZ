package com.farmz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;


import android.content.Context;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class weather extends AppCompatActivity {

    int i=0;
    String encodedCityName = "";
    String precipType;

    EditText cityName;
    ProgressBar precipIntensity;
    ProgressBar precipProbability;
    ProgressBar humidity;
    TextView windSpeed;
    TextView status;
    TextView lattitude; String lat;
    TextView longitude; String lon;
    TextView city; String cit;
    TextView precipIntHead;
    TextView precipProbHead;
    TextView humidityHead;
    TextView timeZone;
    TextView temperature;
    TextView apparentTemperature;
    TextView address;

    LinearLayout mainPage, resPage;

    public void retToMain(View v){

        resPage.setVisibility(View.GONE);
        mainPage.setVisibility(View.VISIBLE);

        precipIntensity.setIndeterminate(true);
        precipProbability.setIndeterminate(true);
        humidity.setIndeterminate(true);
        status.setText("Loading...");
        windSpeed.setText("...");
        lattitude.setText("Latitude: ...");
        longitude.setText("Longitude: ...");
        city.setText("...");
        precipType="Precipitation";
        precipIntHead.setText(precipType+" Intensity: "+"...");
        precipProbHead.setText(precipType+" Probability: "+"...");
        humidityHead.setText("Humidity: "+"...");
        timeZone.setText("...");
        temperature.setText("Temperature: "+"..."+" \u00b0C");
        apparentTemperature.setText("Real-feel: "+"..."+" \u00b0C");
        cityName.setText("");
        address.setText("");

    }

    public void submit(View v){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromInputMethod(cityName.getWindowToken(),0);

        mainPage.setVisibility(View.GONE);
        resPage.setVisibility(View.VISIBLE);

        cit=cityName.getText().toString();
        city.setText(cit);

        DownloadContents task1 = new DownloadContents();

        try {
            encodedCityName = URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            task1.execute("https://maps.googleapis.com/maps/api/geocode/json?&address="+encodedCityName);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public class DownloadContents extends AsyncTask<String,Void ,String> {

        String result="";
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                return e.toString();

            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                //Log.i("result",result);
                JSONArray arr = new JSONArray(jsonObject.getString("results"));
                //Log.i("results",jsonObject.getString("results"));
                JSONObject jsonPart = new JSONObject(arr.getJSONObject(0).getString("geometry"));
                //Log.i("geometry",arr.getJSONObject(0).getString("geometry"));
                JSONArray arr1 = new JSONArray(arr.getJSONObject(0).getString("address_components"));//====================== edit
                for(int j=0;j<arr1.length();j++){
                    address.setText(address.getText().toString()+arr1.getJSONObject(j).getString("long_name")+", ");
                }
                JSONObject jsonPart1 = new JSONObject(jsonPart.getString("location"));
                //Log.i("location",jsonPart.getString("location"));

                lat = jsonPart1.getString("lat");
                //Log.i("lat",lat);
                lon = jsonPart1.getString("lng");
                //Log.i("lon",lon);

                DownloadData task2 = new DownloadData();
                task2.execute("https://api.darksky.net/forecast/42ae16ee27b4df883b136791da85fa3d/"+lat+","+lon);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not retrieve location", Toast.LENGTH_LONG).show();
                city.performClick();
            }

        }
    }

    public class DownloadData extends AsyncTask<String,Void ,String> {

        String result="";
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data=reader.read();
                }

                return result;

            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                DecimalFormat df = new DecimalFormat("####0.00");
                DecimalFormat df1 = new DecimalFormat("####0.0000");

                Log.i("result",result);

                JSONObject jsonObject = new JSONObject(result);

                JSONObject jsonPart = new JSONObject(jsonObject.getString("currently"));

                precipIntensity.setIndeterminate(false);
                precipProbability.setIndeterminate(false);
                humidity.setIndeterminate(false);
                precipIntensity.setProgress((int) (Double.parseDouble(jsonPart.getString("precipIntensity"))*100));
                precipProbability.setProgress((int) (Double.parseDouble(jsonPart.getString("precipProbability"))*100));
                humidity.setProgress((int) (Double.parseDouble(jsonPart.getString("humidity"))*100));
                status.setText(jsonPart.getString("summary"));
                windSpeed.setText(df.format(Double.parseDouble(jsonPart.getString("windSpeed"))*(18/5))+" km/hr");
                lattitude.setText("Latitude: "+df1.format(Double.parseDouble(lat))+"\u00b0");
                longitude.setText("Longitude: "+df1.format(Double.parseDouble(lon))+"\u00b0");
                city.setText(cit);
                try {
                    precipType = jsonPart.getString("precipType");
                    precipType = precipType.substring(0, 1).toUpperCase() + precipType.substring(1);
                }catch (JSONException e){
                    precipType = "Precipitation";
                }
                precipIntHead.setText(precipType+" Intensity: "+df.format(Double.parseDouble(jsonPart.getString("precipIntensity"))*100)+" mm/hr");
                precipProbHead.setText(precipType+" Probability: "+df.format(Double.parseDouble(jsonPart.getString("precipProbability"))*100)+" %");
                humidityHead.setText("Humidity: "+df.format(Double.parseDouble(jsonPart.getString("humidity"))*100)+" %");
                timeZone.setText(jsonObject.getString("timezone"));
                temperature.setText("Temperature: "+df.format((Double.parseDouble(jsonPart.getString("temperature"))-32)*.5556)+" \u00b0C");
                apparentTemperature.setText("Real-feel: "+df.format((Double.parseDouble(jsonPart.getString("apparentTemperature"))-32)*.5556)+" \u00b0C");

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not retrieve weather", Toast.LENGTH_LONG).show();
                city.performClick();
            }

            Log.i("result",result);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityName = (EditText) findViewById(R.id.cityName);
        precipIntensity = (ProgressBar) findViewById(R.id.precipIntensity);
        precipProbability = (ProgressBar) findViewById(R.id.precipProbability);
        humidity = (ProgressBar) findViewById(R.id.humidity);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        status = (TextView) findViewById(R.id.status);
        lattitude = (TextView) findViewById(R.id.lattitude);
        longitude = (TextView) findViewById(R.id.longitude);
        city = (TextView) findViewById(R.id.city);
        precipIntHead = (TextView) findViewById(R.id.precipIntHead);
        precipProbHead = (TextView) findViewById(R.id.precipProbHead);
        humidityHead = (TextView) findViewById(R.id.humidityHead);
        timeZone = (TextView) findViewById(R.id.timeZone);
        temperature = (TextView) findViewById(R.id.temperature);
        apparentTemperature = (TextView) findViewById(R.id.apparentTemperature);
        mainPage = (LinearLayout) findViewById(R.id.mainPage);
        resPage = (LinearLayout) findViewById(R.id.resPage);
        address = (TextView) findViewById(R.id.address);

        resPage.setVisibility(View.GONE);
        mainPage.setVisibility(View.VISIBLE);

        precipIntensity.setMax(100);
        precipProbability.setMax(100);
        humidity.setMax(100);
        city.performClick();
    }

}