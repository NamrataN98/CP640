package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {

    public String weatherurl = "";
    public String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar pgBar = (ProgressBar) findViewById(R.id.progressBar);
        pgBar.setVisibility(View.VISIBLE);

        city = getIntent().getStringExtra("cityName");
        weatherurl = "https://api.openweathermap.org/data/2.5/weather?" + "q=" + city + ",ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&mode=xml&units=metric";

        ForecastQuery query1 = (ForecastQuery) new ForecastQuery().execute(weatherurl);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        public String currentTemp;
        public String minTemp;
        public String maxTemp;
        public Bitmap weatherimg;
        String ns = null;


        @Override
        public String doInBackground(String ...strings)  {
            try {
                InputStream data = HttpConnectionDownload();
                StartParser(data);

            } catch (IOException e ) {
                Log.i("IO ERROR",e.toString());
            } catch (XmlPullParserException e) {
                Log.i("XML PARSER ERROR",e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ProgressBar pgBar = (ProgressBar) findViewById(R.id.progressBar);
            pgBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView minT = findViewById(R.id.minTemp);
            TextView maxT = findViewById(R.id.maxTemp);
            TextView current = findViewById(R.id.currentTemp);
            ImageView img = findViewById(R.id.imageView3);

            minT.setText("Minimum : " + minTemp + " " + "C\u00b0");
            maxT.setText("Maximum : " + maxTemp + " " + "C\u00b0");
            current.setText("Current : " + currentTemp + " " + "C\u00b0");
            img.setImageBitmap(weatherimg);
            ProgressBar pgBar = (ProgressBar) findViewById(R.id.progressBar);
            pgBar.setVisibility(View.INVISIBLE);
        }

        public InputStream HttpConnectionDownload() throws IOException {

            URL url = new URL(weatherurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }

        public void StartParser(InputStream data) throws XmlPullParserException, IOException {

            try {

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(data, null);
                parser.nextTag();

                parser.require(XmlPullParser.START_TAG, ns, "current");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tag = parser.getName();
                    Log.i("TAG", tag);
                    if (tag.equals("temperature")) {
                        String title = readTemp(parser);
                        Log.i("value", title);
                    } else if (tag.equals("weather")) {
                        String weatherIcon = readWeather(parser);
                        Log.i("Weather", weatherIcon);

                        String ImageURL = "http://openweathermap.org/img/w/" + weatherIcon + ".png";
                        String fileName = weatherIcon + ".png";

                        if (fileExistence(fileName)) {
                            FileInputStream fis = null;
                            try {
                                fis = openFileInput(fileName);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Log.i("Found the file locally",fileName);
                            weatherimg = BitmapFactory.decodeStream(fis);
                        } else {
                            String iconUrl =
                                    "https://openweathermap.org/img/w/"
                                            + fileName;
                            weatherimg = getImage(new URL(iconUrl));
                            FileOutputStream outputStream =
                                    openFileOutput(fileName, Context.MODE_PRIVATE);
                            weatherimg.compress(Bitmap.CompressFormat.PNG,
                                    80, outputStream);
                            Log.i("Downloaded the file from the Internet",fileName);
                            outputStream.flush();
                            outputStream.close();
                        }
                        publishProgress(100);

                    } else {
                        skip(parser);
                    }
                }

            }
            finally {
                data.close();
            }
        }

        private String readTemp(XmlPullParser parser) throws IOException, XmlPullParserException {
            String link = "";
            parser.require(XmlPullParser.START_TAG, ns, "temperature");
            String tag = parser.getName();
            currentTemp = parser.getAttributeValue(null, "value");
            publishProgress(25);
            minTemp = parser.getAttributeValue(null, "min");
            publishProgress(50);
            maxTemp = parser.getAttributeValue(null, "max");
            publishProgress(75);
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "temperature");
            return link;
        }

        private String readWeather(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "weather");
            String icon  = parser.getAttributeValue(null, "icon");
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, "weather");
            return icon;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
    }

    public boolean fileExistence (String fileName){
        File file = getFileStreamPath(fileName);
        return file.exists();
    }

    public Bitmap getImage (URL url){
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}