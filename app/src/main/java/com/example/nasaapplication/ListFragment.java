package com.example.nasaapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class ListFragment extends Fragment {
    ListView nasaList;
    ListAdapter MyAdapter;
    SQLiteDatabase db;
    Button clear;
    ArrayList<nasaObject> nasaElementList = new ArrayList<>();

    EditText listSearch;


    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listSearch = view.findViewById(R.id.listSearchBar);
        clear = view.findViewById(R.id.clearButton);
        clear.setOnClickListener(clickedView -> {
            Snackbar.make(view, (getResources().getString(R.string.snacky)), Snackbar.LENGTH_SHORT).show();
            // TODO: deleteList(nasaObject c);
        });

        nasaList = view.findViewById(R.id.nasalist);

        nasaElementList.clear();
        loadDataFromDatabase();

        nasaList.setAdapter(MyAdapter = new ListAdapter());
        MyAdapter.setData(nasaElementList);

        nasaList.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getResources().getString(R.string.doYouTitle))
                    .setPositiveButton(getResources().getString(R.string.yesButton), (click, arg) -> {
                        nasaElementList.remove(pos);
                        MyAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getResources().getString(R.string.noButton), (click,arg) -> {})
                    .setView(getLayoutInflater().inflate(R.layout.activity_list, null))
                    .create().show();
            return true;
        });

        SwipeRefreshLayout refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener( () -> refresher.setRefreshing(false)  );

        nasaList.setOnItemClickListener((parent, view1, position, id) -> {
            nasaObject nasaPass = MyAdapter.getItem(position);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcv, ImageFragment.newInstance(nasaPass.getNasaUrl(), nasaPass.getNasaDate(), nasaPass.getNasaTitle(), nasaPass.getNasaExplain()))
                    .addToBackStack(null)
                    .commit();
        });

        nasaList.setOnItemLongClickListener((p, b, pos, id) -> {
            selectList(pos);
            return true;
        });

        listSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase();
                ArrayList<nasaObject> filteredNasaList = new ArrayList<>();
                if (!searchText.isEmpty()) {
                    for (nasaObject nasa : nasaElementList) {
                        if (nasa.getNasaTitle().toLowerCase().contains(searchText)
                                || nasa.getNasaDate().contains(searchText)) {
                            filteredNasaList.add(nasa);
                        }
                    }
                    MyAdapter.setData(filteredNasaList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void loadDataFromDatabase() {
        SQLDatabase openDB = new SQLDatabase(getContext());
        db = openDB.getWritableDatabase();


        String [] columns = {
                SQLDatabase.COL_DATE, SQLDatabase.COL_TITLE, SQLDatabase.COL_EXPLAIN, SQLDatabase.COL_URL
        };

        Cursor results = db.query(false, SQLDatabase.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColIndex = results.getColumnIndex(SQLDatabase.COL_TITLE);
        int explanationColIndex = results.getColumnIndex(SQLDatabase.COL_EXPLAIN);
        int dateColIndex = results.getColumnIndex(SQLDatabase.COL_DATE);
        int urlColIndex = results.getColumnIndex(SQLDatabase.COL_URL);

        while(results.moveToNext())
        {
            String title = results.getString(titleColIndex);
            String date = results.getString(dateColIndex);
            String explain = results.getString(explanationColIndex);
            String url = results.getString(urlColIndex);
            nasaElementList.add(new nasaObject(title, date, explain, url));
        }

    }

    protected void selectList(int position) {

        nasaObject selectObj = nasaElementList.get(position);

        View activity_view = getLayoutInflater().inflate(R.layout.activity_lists, null);

        TextView rowTxt = activity_view.findViewById(R.id.textView2);

        rowTxt.setText(selectObj.getNasaDate());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
            db.delete(SQLDatabase.TABLE_NAME, SQLDatabase.COL_DATE + "= ?", new String[] {c.getNasaDate()});
        }

    class ListAdapter extends BaseAdapter {

        ArrayList<nasaObject> listItems = new ArrayList<>();

        public void setData(ArrayList<nasaObject> data) {
            listItems.clear();
            listItems.addAll(data);
            notifyDataSetChanged();
        }

        public int getCount() {
            return listItems.size();
        }

        public nasaObject getItem(int position) {
            return listItems.get(position);
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
            TextView newTxt1 = newView.findViewById(R.id.textView2);
            TextView newTxt2 = newView.findViewById(R.id.textView3);

            newTxt1.setText(thisRow.getNasaTitle());
            newTxt2.setText(thisRow.getNasaDate());

            return newView;
        }

    }

    public class nasaObject {
        String nasaTitle;
        String nasaDate;
        String nasaExplain;
        String nasaUrl;

        public nasaObject(String nasaTitle, String nasaDate, String nasaExplain, String nasaUrl) {
            this.nasaTitle = nasaTitle;
            this.nasaDate = nasaDate;
            this.nasaExplain = nasaExplain;
            this.nasaUrl = nasaUrl;
        }

        public String getNasaTitle() {
            return nasaTitle;
        }

        public String getNasaDate() {
            return nasaDate;
        }

        public String getNasaExplain() {
            return nasaExplain;
        }

        public String getNasaUrl() {
            return nasaUrl;
        }
    }
}