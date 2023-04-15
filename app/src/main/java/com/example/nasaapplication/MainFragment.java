package com.example.nasaapplication;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

/**
 * The MainFragment fragment parses the json file relevant to the date the user selected in the DateActivity.
 * MainFragment will inflate in FCV container on MainActivity.
 * Save button allows user to save the image to their list and to SQLdatabase, including the title, date and explanation from the json file.
 *
 * Authors: Damon & Dylan
 *
 */
public class MainFragment extends Fragment {
    /**
     * Declaration of variables
     */
    ImageView imgView;
    Button saveButton;
    String userDate;
    SQLiteDatabase db;
    String nasaTitle;
    String nasaDate;
    String nasaExplanation;
    String nasaUrl;
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    /**
     * nasaImages class extends AsyncTask
     */
    class nasaImages extends AsyncTask<String, Integer, Bitmap> {

        Bitmap nasaPic;

        /**
         * Bitmap doinBackground opens a HTTP connection to the url corresponding to the date selected.
         * The jsonfile will be parsed and return values of "date, title, url, explanation".
         * url returned by jsonfile will be accessed to download the image and save it to file storage on phone.
         * @param args The parameters of the task.
         *
         * @return nasaPic
         */
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
                nasaDate = nasaImg.getString("date");
                nasaTitle = nasaImg.getString("title");
                nasaUrl = nasaImg.getString("url");
                nasaExplanation = nasaImg.getString("explanation");
                URL nasaPicUrl = new URL(nasaUrl);
                response.close();

                BufferedInputStream bis = new BufferedInputStream(nasaPicUrl.openStream());
                nasaPic = BitmapFactory.decodeStream(bis);
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path.getAbsolutePath() + "/Pictures");
                dir.mkdir();
                Log.d("File Path", "File Path is: " + dir);
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

        /**
         * sets the imgView to the image saved from the doinBackground. Displaying the image to the user.
         * @param args The values indicating progress.
         *
         */
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

    /**
     * method that after the View is created will allow users to interact with the Save button.
     * if users press the save button, the image and its title, date and explanation will be saved to their list
     * Will also insert data into the database, toast will notify user if the item is saved successfully or if the item is already in their list/database
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveButton = view.findViewById(R.id.saveButton);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        userDate = sharedPref.getString("date", null);
        Log.d("User Date from sharedPref", "values is " + userDate);
        imgView = view.findViewById(R.id.imageView);
        nasaImages req = new nasaImages();
        req.execute("https://api.nasa.gov/planetary/apod?api_key=acQMNXflx5bSpHDHl3vc7uLnLYvCchnUDoHxGD0t&date="+userDate);

        saveButton.setOnClickListener(v -> {
            long rcode = insertData(nasaTitle, nasaExplanation, nasaDate, nasaUrl);
            if (rcode == -1){
                Toast.makeText(getContext(),"Image is already in your list", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Image Saved to your List!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * insertData inserts the values parsed from the json of the date users selected, and have decided to save
     * @param nasaTitle
     * @param nasaExplanation
     * @param nasaDate
     * @param nasaUrl
     * @return
     */
    private long insertData(String nasaTitle, String nasaExplanation, String nasaDate, String nasaUrl) {
        SQLDatabase dbopener = new SQLDatabase(getContext());
        db = dbopener.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(dbopener.COL_DATE, nasaDate);
        cValues.put(dbopener.COL_EXPLAIN, nasaExplanation);
        cValues.put(dbopener.COL_TITLE, nasaTitle);
        cValues.put(dbopener.COL_URL, nasaUrl);
        Log.d("Values inserted into DB", "values are " + nasaDate + nasaTitle + nasaExplanation + nasaUrl);
        return db.insert(dbopener.TABLE_NAME, null, cValues);
    }
}

