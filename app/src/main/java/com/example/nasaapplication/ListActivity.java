package com.example.nasaapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
/**
 *  The NASA Application my list page gives users the ability to save their images of the day to a listview.
 *  Users can press on each item on the list to receive more details, long press to delete the item or press clear to delete the whole list.
 *
 *  Authors: Damon & Dylan
 *
 */
public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /**
     * Declare each item as a variable.
     */
    Toolbar tool;
    DrawerLayout draw;
    /**
     * When the activity is created it will create each declared item above along with the action bar, navigation view with a drawer to switch activities, and a fragment manager used to view the image.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tool = findViewById(R.id.my_toolbar);
        setSupportActionBar(tool);

        draw = findViewById(R.id.my_drawer);
        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, draw, tool, R.string.open, R.string.close);
        draw.addDrawerListener(abdToggle);
        abdToggle.syncState();

        NavigationView navigationView = findViewById(R.id.naviView);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fcv, ListFragment.newInstance()).commit();
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
                Intent nextPaged = new Intent(ListActivity.this, HowActivity.class);
                startActivity(nextPaged);
                break;
            case R.id.datepick:
                Intent listPage = new Intent(ListActivity.this, DateActivity.class);
                startActivity(listPage);
                break;
            case R.id.myhome:
                Intent nextPage = new Intent(ListActivity.this, MainActivity.class);
                startActivity(nextPage);
                break;
            case R.id.mylist:
                message = "You are already here silly!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
        }
        draw.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * onOptionsItemSelected controls the toolbar menu items and depending on which is pressed either a settings activity will start or a toast will appear and exit the application.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.item1:
                Intent nextPages = new Intent(ListActivity.this, Settings.class);
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
