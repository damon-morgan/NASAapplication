package com.example.nasaapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ImageFragment extends Fragment {

    private static final String ARG_IMAGE = "image";
    private static final String ARG_DATE = "date";
    private static final String ARG_TITLE = "title";
    private static final String ARG_EXPLANATION = "explanation";
    private String image;
    private String date;
    private String title;
    private String explanation;

    ImageView imFrag;
    TextView tvFrag1;
    TextView tvFrag2;
    TextView tvFrag3;



    public ImageFragment() {
        // Required empty public constructor
    }
    public static ImageFragment newInstance(String image, String date, String title, String explanation) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_DATE, date);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_EXPLANATION, explanation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getString(ARG_IMAGE);
            date = getArguments().getString(ARG_DATE);
            title = getArguments().getString(ARG_TITLE);
            explanation = getArguments().getString(ARG_EXPLANATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imFrag = view.findViewById(R.id.nasaListImage);
        tvFrag1 = view.findViewById(R.id.nasaListTitle);
        tvFrag2 = view.findViewById(R.id.nasaListDate);
        tvFrag3 = view.findViewById(R.id.nasaListExplain);

        new LoadImageTask().execute(image);

        tvFrag1.setText(title);
        tvFrag2.setText(date);
        tvFrag3.setText(explanation);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imFrag.setImageBitmap(result);
            }
        }
    }
}