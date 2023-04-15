package com.example.nasaapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
/**
 *  The NASA Application settings page gives users option to print a greeting.
 *  User can press the print button to call on a snackbar with a message.
 *
 *  Authors: Damon & Dylan
 *
 */
public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /**
     * Declare each item as a variable.
     */
    Toolbar tool;
    DrawerLayout draw;
    Button printButton;

    /**
     * When the activity is created it will create each declared item above along with the action bar, navigation view with a drawer to switch activities, a print button to show a snackbar with a message when clicked.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tool = findViewById(R.id.my_toolbar);
        setSupportActionBar(tool);
        draw = findViewById(R.id.my_drawer);
        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, draw, tool, R.string.open, R.string.close);
        draw.addDrawerListener(abdToggle);
        abdToggle.syncState();

        NavigationView navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        printButton = findViewById(R.id.Print);
        printButton.setOnClickListener(view -> {
            Snackbar.make(view, (getResources().getString(R.string.snacky2)), Snackbar.LENGTH_SHORT).show();
        });
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
                Intent nextPage = new Intent(Settings.this, HowActivity.class);
                startActivity(nextPage);
                break;
            case R.id.datepick:
                Intent listPage = new Intent(Settings.this, DateActivity.class);
                startActivity(listPage);
                break;
            case R.id.myhome:
                Intent nextPages = new Intent(Settings.this, MainActivity.class);
                startActivity(nextPages);
                break;
            case R.id.mylist:
                Intent nextPaged = new Intent(Settings.this, ListActivity.class);
                startActivity(nextPaged);
                break;
        }
        draw.closeDrawer(GravityCompat.START);


        return true;
    }


    /**
     * onOptionsItemSelected controls the toolbar menu items, help alert dialog and a toast will appear. Depending on which is selected, exit the application.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                message = "You are already here silly!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                message = "Exiting the application";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
            case R.id.item3:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.helptitle))
                        .setMessage(getResources().getString(R.string.settinghelp))
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
