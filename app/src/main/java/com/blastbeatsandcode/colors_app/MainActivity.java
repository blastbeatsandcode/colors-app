package com.blastbeatsandcode.colors_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button _randomizeButton;
    private Button _switchToDrawingActivity;

    private EditText _userEntryText;
    private TextView _rgbText;
    private TextView _hexText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Map private elements to their respective IDs
        _randomizeButton = findViewById(R.id.btn_randomize_color);
        _userEntryText = findViewById(R.id.txt_user_entry);
        _rgbText = findViewById(R.id.lbl_rgb_color_value);
        _hexText = findViewById(R.id.lbl_hex_color_value);
        _switchToDrawingActivity = findViewById(R.id.btn_switch_to_drawing);

        // Map elements to functions
        _randomizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorizeText();
            }
        });

        _switchToDrawingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDrawingActivity();
            }
        });

    }

    /* Colorizes the text in the window */
    private void ColorizeText()
    {
        int r = GetRandomSingleRGBValue();
        int g = GetRandomSingleRGBValue();
        int b = GetRandomSingleRGBValue();

        _userEntryText.setTextColor(Color.argb(255, r, g, b));
        SetColorStrings(r, g, b);
    }

    /* Get a random value for an RGB value */
    private int GetRandomSingleRGBValue()
    {
        Random r = new Random();
        return r.nextInt(256);
    }

    /* Return the color value string */
    private void SetColorStrings(int r, int g, int b)
    {
        _rgbText.setText(r + "r, " + g + "g, " + b + "b");
        _hexText.setText("#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b));
    }

    /* Start Drawing activity */
    private void StartDrawingActivity()
    {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }
}
