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

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar tool;
    DrawerLayout draw;
    ListView nasaList;
    ListAdapter MyAdapter;
    SQLiteDatabase db;
    ArrayList<nasaObject> nasaElementList = new ArrayList<>();

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

        nasaList = findViewById(R.id.nasalist);

        loadDataFromDatabase();

        nasaList.setAdapter(MyAdapter = new ListAdapter());

        nasaList.setOnItemLongClickListener((p, b, pos, id) -> {
            selectList(pos);
            return true;
        });


    }

    private void loadDataFromDatabase() {
        SQLDatabase openDB = new SQLDatabase(this);
        db = openDB.getWritableDatabase();


        String [] columns = {
                SQLDatabase.COL_DATE, SQLDatabase.COL_TITLE, SQLDatabase.COL_EXPLAIN
        };

        Cursor results = db.query(false, SQLDatabase.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColIndex = results.getColumnIndex(SQLDatabase.COL_TITLE);
        int explanationColIndex = results.getColumnIndex(SQLDatabase.COL_EXPLAIN);
        int dateColIndex = results.getColumnIndex(SQLDatabase.COL_DATE);

        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String date = results.getString(dateColIndex);
            String explain = results.getString(explanationColIndex);
            nasaElementList.add(new nasaObject(title, date, explain));
        }

    }

    protected void selectList(int position) {

        nasaObject selectObj = nasaElementList.get(position);

        View activity_view = getLayoutInflater().inflate(R.layout.activity_lists, null);

        TextView rowTxt = activity_view.findViewById(R.id.textView2);

        rowTxt.setText(selectObj.getDate());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.nasatitle))
                .setMessage("Get detailed information")
                .setView(activity_view)
                .setPositiveButton(getResources().getString(R.string.deletebutton), (click, arg) -> {
                    deleteList(selectObj);
                    nasaElementList.remove(position);
                    MyAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(getResources().getString(R.string.backbutton), (click,arg) -> {})
                .create().show();


    }

    protected void deleteList(nasaObject c) {
        db.delete(SQLDatabase.TABLE_NAME, SQLDatabase.COL_DATE + "= ?", new String[] {c.getDate()});
    }

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

    class ListAdapter extends BaseAdapter {

        public int getCount() {
            return nasaElementList.size();
        }

        public nasaObject getItem(int position) {
            return nasaElementList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View newView = convertView;
            LayoutInflater inflater = getLayoutInflater();

            nasaObject thisRow = getItem(position);
            if(newView == null) {
                newView = inflater.inflate(R.layout.activity_lists, parent, false);
            }
            TextView newTxt = newView.findViewById(R.id.textView2);

            newTxt.setText(thisRow.getDate());

            return newView;
        }

    }

    public class nasaObject {
        String nasaTitle;
        String nasaDate;
        String nasaExplain;

        public nasaObject(String nasaTitle, String nasaDate, String nasaExplain) {
            this.nasaTitle = nasaTitle;
            this.nasaDate = nasaDate;
            this.nasaExplain = nasaExplain;
        }

        public String getTitle() {
            return nasaTitle;
        }

        public String getDate() {
            return nasaDate;
        }

        public String getExplain() {
            return nasaExplain;
        }

    }

}
