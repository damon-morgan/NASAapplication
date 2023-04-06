package com.example.nasaapplication;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar tool;
    DrawerLayout draw;
    //ProgressBar progressBar;
    ImageView imgView;
    String userDate;

    // Unique API Key: acQMNXflx5bSpHDHl3vc7uLnLYvCchnUDoHxGD0t

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tool = findViewById(R.id.my_toolbar);
        setSupportActionBar(tool);
        draw = findViewById(R.id.my_drawer);
        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, draw, tool, R.string.open, R.string.close);
        draw.addDrawerListener(abdToggle);
        abdToggle.syncState();

        NavigationView navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharepref = getSharedPreferences("nasaPrefs", Context.MODE_PRIVATE);
        userDate = sharepref.getString("date", null);

        imgView = findViewById(R.id.imageView);
        nasaImages req = new nasaImages();
        req.execute("https://api.nasa.gov/planetary/apod?api_key=acQMNXflx5bSpHDHl3vc7uLnLYvCchnUDoHxGD0t&date="+userDate);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId()) {
            case R.id.howto:
                Intent nextPage = new Intent(MainActivity.this, HowActivity.class);
                startActivity(nextPage);
                break;
            case R.id.datepick:
                Intent listPage = new Intent(MainActivity.this, DateActivity.class);
                startActivity(listPage);
                break;
            case R.id.myhome:
                message = "You are already here silly!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mylist:
                Intent nextPaged = new Intent(MainActivity.this, ListActivity.class);
                startActivity(nextPaged);
                break;
        }
        draw.closeDrawer(GravityCompat.START);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                // TODO: Implement settings activity
                // TODO: IDEA: Settings can include clearing list completely, change background of application
                break;
            case R.id.item2:
                message = "Exiting the application";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
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
}