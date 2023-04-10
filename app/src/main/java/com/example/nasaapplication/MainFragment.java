package com.example.nasaapplication;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainFragment extends Fragment {

    ImageView imgView;
    String userDate;
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    class nasaImages extends AsyncTask<String, Integer, Bitmap> {

        Bitmap nasaPic;

        @Override
        protected Bitmap doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnect.getInputStream();

                BufferedReader read = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = read.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                JSONObject nasaImg = new JSONObject(result);
                String nasaDate = nasaImg.getString("date");
                String nasaTitle = nasaImg.getString("title");
                String nasaUrl = nasaImg.getString("url");
                URL nasaPicUrl = new URL(nasaUrl);
                response.close();

                BufferedInputStream bis = new BufferedInputStream(nasaPicUrl.openStream());
                nasaPic = BitmapFactory.decodeStream(bis);
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path.getAbsolutePath() + "/Pictures");
                dir.mkdir();
                File nasaFile = new File(dir, nasaDate + ".png");
                if (nasaFile.exists()) {
                    FileOutputStream outputStream = new FileOutputStream(nasaFile);
                    nasaPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    nasaPic = BitmapFactory.decodeFile(path.getAbsolutePath());
                    outputStream.flush();
                    outputStream.close();
                }
                bis.close();

                for (int i = 0; i < 100; i++) {
                    try {
                        publishProgress(i);
                        Thread.sleep(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return nasaPic;
        }

        public void onProgressUpdate(Integer... args) {
            //progressBar.setProgress(args[0]);
            imgView.setImageBitmap(nasaPic);
        }

        public void onPostExecute(Bitmap catPic) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        userDate = sharedPref.getString("date", null);
        Log.d("User Date from sharedPref", "values is " + userDate);
        imgView = view.findViewById(R.id.imageView);
        nasaImages req = new nasaImages();
        req.execute("https://api.nasa.gov/planetary/apod?api_key=acQMNXflx5bSpHDHl3vc7uLnLYvCchnUDoHxGD0t&date="+userDate);
    }
}