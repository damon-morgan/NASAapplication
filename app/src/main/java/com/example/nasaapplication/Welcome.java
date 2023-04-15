package com.example.nasaapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 *  The NASA Application welcome page to greet users when they execute the application.
 *  User can press the enter button to access the image of the day.
 *
 *  Authors: Damon & Dylan
 *
 */

public class Welcome extends AppCompatActivity {

    /**
     * Declare the button as a variable.
     */
    Button enterButton;

    /**
     * When the activity is created it will create a button with an onclick listener waiting for the user to activate it.
     * @param savedInstanceState
     */
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
