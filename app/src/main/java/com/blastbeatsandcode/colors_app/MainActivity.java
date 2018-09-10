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

/*
*   MainActivity is the initial activity of the application.
*   Users can enter a string of text at the top and press a button to randomly generate a color
*   and change the text color to it. The view will also display the color's RGB and hex values.
* */
public class MainActivity extends AppCompatActivity {

    // Member variables; contains references to the buttons and textviews of the view
    private Button _randomizeButton;
    private Button _switchToDrawingActivity;
    private EditText _userEntryText;
    private TextView _rgbText;
    private TextView _hexText;

    /*
    *   Creates the MainActivity. Maps buttons to member variables and assigns
    *   function calls to the buttons.
    * */
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
        // Randomize color
        _randomizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorizeText();
            }
        });

        // Switch to the Drawing activity
        _switchToDrawingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDrawingActivity();
            }
        });

    }

    /*
    *   Colorizes text in the window by calling the GetRandomSingleRGBValue() function multiple times.
    *   The textcolor is then set by each individual value with a default alpha value of 255.
    *   This then calls SetColorStrings() which updates the color values in the label in the view.
    * */
    private void ColorizeText()
    {
        // Randomly generate an RGB value
        int r = GetRandomSingleRGBValue();
        int g = GetRandomSingleRGBValue();
        int b = GetRandomSingleRGBValue();

        _userEntryText.setTextColor(Color.argb(255, r, g, b)); // Set colors
        SetColorStrings(r, g, b); // Update color values in labels
    }

    /*
    *   Get a random number for an RGB value
    * */
    private int GetRandomSingleRGBValue()
    {
        Random r = new Random();
        return r.nextInt(256);
    }

    /*
    *   Set the color strings in the view to the given RGB values.
    * */
    private void SetColorStrings(int r, int g, int b)
    {
        _rgbText.setText(r + "r, " + g + "g, " + b + "b");
        _hexText.setText("#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b));
    }

    /*
    *   Starts the Drawing activity
    * */
    private void StartDrawingActivity()
    {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }
}
