package com.example.nasaapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar tool;
    DrawerLayout draw;
    TextView selectDate;
    Button dateSelectButton, retrieveButton;
    String selectedDate;
    SharedPreferences sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        tool = findViewById(R.id.my_toolbar);
        setSupportActionBar(tool);
        draw = findViewById(R.id.my_drawer);
        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, draw, tool, R.string.open, R.string.close);
        draw.addDrawerListener(abdToggle);
        abdToggle.syncState();

        NavigationView navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);
        selectDate = findViewById(R.id.selectDate);
        dateSelectButton = findViewById(R.id.dateSelectButton);
        retrieveButton = findViewById(R.id.retrieveButton);
        sharePref = getSharedPreferences("nasaPrefs", Context.MODE_PRIVATE);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateSelectButton.setOnClickListener(view -> {

            DatePickerDialog dialog = new DatePickerDialog(DateActivity.this, (view1, year1, month1, day1) -> {
                month1 = month1 +1;
                String date = year1 +"-"+ month1 +"-"+ day1;
                selectedDate = date;
                dateSelectButton.setText(date);

            },year, month,day);
            dialog.show();

        });

        retrieveButton.setOnClickListener(view -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            Log.d("User Date on Retrieve", "value is: " + selectedDate);
            editor.putString("date", selectedDate);
            editor.commit();
            Intent nasaPage = new Intent(DateActivity.this, MainActivity.class);
            startActivity(nasaPage);
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message;
        switch(item.getItemId()) {
            case R.id.howto:
                Intent listPage = new Intent(DateActivity.this, HowActivity.class);
                startActivity(listPage);
                break;
            case R.id.datepick:
                message = "You are already here silly!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.myhome:
                Intent nextPage = new Intent(DateActivity.this, MainActivity.class);
                startActivity(nextPage);
                break;
            case R.id.mylist:
                Intent nextPaged = new Intent(DateActivity.this, ListActivity.class);
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
                Intent nextPages = new Intent(DateActivity.this, Settings.class);
                startActivity(nextPages);
                break;
            case R.id.item2:
                message = "Exiting the application";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }
}
