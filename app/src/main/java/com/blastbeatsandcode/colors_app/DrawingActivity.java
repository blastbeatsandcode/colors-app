package com.blastbeatsandcode.colors_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blastbeatsandcode.colors_app.R;

import java.util.Calendar;

import top.defaults.colorpicker.ColorPickerPopup;

/*
*   Allows the user to draw on the screen.
*   Allows the saving of the image, clearing of the drawn image,
*   or selecting colors to draw with.
* */
public class DrawingActivity extends Activity {
    // Member variables, includes buttons and the custom drawing view
    private Button _btnSave;
    private Button _btnClear;
    private Button _btnSwitchToTextEntry;
    private Button _btnColorPicker;
    private DrawingCanvasView _drawingView;

    /*
    *   Creates the activity and maps buttons to functions.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        // Get buttons from view
        _btnSave = findViewById(R.id.btn_save);
        _btnClear = findViewById(R.id.btn_clear);
        _btnSwitchToTextEntry = findViewById(R.id.btn_switch_to_user_entry);
        _drawingView = findViewById(R.id.drawing_area);
        _btnColorPicker = findViewById(R.id.btn_color_picker);

        // Send buttons to the drawingView so they can be transparent when user is drawing
        _drawingView.AddButton(_btnClear);
        _drawingView.AddButton(_btnSave);
        _drawingView.AddButton(_btnColorPicker);
        _drawingView.AddButton(_btnSwitchToTextEntry);

        // Map buttons to functions
        // Switch to text entry activity
        _btnSwitchToTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartUserEntryActivity();
            }
        });

        // Clear the canvas
        _btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.ClearCanvas();
            }
        });

        // Save the canvas as an image
        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCanvas();
            }
        });

        // Open color picker
        _btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPick(v);
            }
        });
    }

    /*
    *   Save the user created image.
    * */
    public void SaveCanvas()
    {
        // First check to see if the app has permission to save the image
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }

        // Show dialog to ask user to save image
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save Drawing");
        saveDialog.setMessage("Save drawing to gallery?");

        // If user selects "Yes"
        saveDialog.setPositiveButton("Save", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                // Generate a random file name based on current milliseconds and save the image as a PNG
                String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
                Bitmap bmp = loadBitmapFromView(_drawingView);

                // Here we save the image to the gallery
                String saved = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, fileName + ".png", "Drawing");

                // Show toast to show if image has been saved or not
                if (saved != "")
                    Toast.makeText(getBaseContext(), "Image saved to Gallery", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), "Image could not be saved.", Toast.LENGTH_LONG).show();
            }
        });

        // If user selects "Cancel"
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });

        // Show the save dialog
        saveDialog.show();
    }

    /*
    *   Takes in a view and creates a bitmap from its appearance.
    *   This is used to create an image of the user's drawing.
    *   Method is a modified version of code by Laura SuciuSnippets:
    *   https://www.myandroidsolutions.com/2013/02/10/android-get-view-drawimage-and-save-it/#.W5WwdRgpBhE
    * */
    public static Bitmap loadBitmapFromView(View view) {

        // Get height and width of the view
        int w = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        int h = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredHeight(), View.MeasureSpec.AT_MOST);

        // Set measurement constraints
        view.measure(w, h);
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(),
                view.getMeasuredHeight() + view.getTop());

        // Create a bitmap based on the view; this is the image we will save
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas and draw the bitmap onto it
        Canvas canvas = new Canvas(bitmap);

        // Position the bitmap inside of the canvas
        canvas.translate(-view.getScrollX(), -view.getScrollY());

        // Draw onto the canvas
        view.draw(canvas);

        // Return the created image
        return bitmap;
    }


    /*
    *   Switches back to the user entry activity
    * */
    private void StartUserEntryActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
    *   Opens the color picker window.
    *   When the user Selects a color, it sets the paint in the drawing view to that color
    *   and updates the background of the color picker button to the newly selected color.
    * */
    private void ColorPick(View v)
    {
        new ColorPickerPopup.Builder(this)
            .initialColor(_drawingView.GetPaintColor()) // Set initial color to the default paint color
            .enableAlpha(true)                          // Enable alpha slider
            .okTitle("Choose")                          // Set title for the OK button
            .cancelTitle("Cancel")                      // Set title for the CANCEL button
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(v, new ColorPickerPopup.ColorPickerObserver() {
                @Override
                public void onColorPicked(int color) {
                    _drawingView.SetPaint(color);               // Set paint color
                    _btnColorPicker.setBackgroundColor(color);  // Set button color
                }
                @Override
                public void onColor(int color, boolean fromUser) {
                    // Do nothing
                }
            });
    }
}
