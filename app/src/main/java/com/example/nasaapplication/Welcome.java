package com.example.nasaapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        enterButton = findViewById(R.id.enterbutton);

        enterButton.setOnClickListener(view -> {
            Intent nextPage = new Intent(Welcome.this, HowActivity.class);
            startActivity(nextPage);
        });
    }
}
