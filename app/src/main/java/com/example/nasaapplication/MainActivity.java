package com.example.nasaapplication;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

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
 *  The NASA Application Image of the Day page allows users to view the image of the day based on the date they selected.
 *  User can press the add button to save the image of the day to my list.
 *
 *  Authors: Damon & Dylan
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Declare each item as a variable.
     */
    Toolbar tool;
    DrawerLayout draw;
    //ProgressBar progressBar;
    ImageView imgView;
    String userDate;
    Button addButton;

    // Unique API Key: acQMNXflx5bSpHDHl3vc7uLnLYvCchnUDoHxGD0t

    /**
     * When the activity is created it will create each item declared above along with the action bar, navigation view with a drawer to switch activities, and a fragment manager used to view the image.
     * @param savedInstanceState
     */
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fcv, MainFragment.newInstance()).commit();
    }

    /**
     * onNavigationItemSelected controls the navigation menu items and when each are pressed they send the user to a new activity based on selection.
     * @param item
     * @return
     */
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

    /**
     * onOptionsItemSelected controls the toolbar menu items and depending on which is pressed either a settings activity will start, help alert dialog will appear or a toast will appear and exit the application.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                Intent nextPages = new Intent(MainActivity.this, Settings.class);
                startActivity(nextPages);
                break;
            case R.id.item2:
                message = "Exiting the application";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
            case R.id.item3:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.helptitle))
                        .setMessage(getResources().getString(R.string.mainhelp))
                        .setNegativeButton(getResources().getString(R.string.backbutton), (click,arg) -> {})
                        .create().show();
                break;
        }

        return true;
    }

    /**
     * onCreateOptionsMenu adds a menu view to the activity.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }
}