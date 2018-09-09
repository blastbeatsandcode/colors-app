package com.blastbeatsandcode.colors_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blastbeatsandcode.colors_app.R;

import java.util.Calendar;

public class DrawingActivity extends Activity {

    private Button _btnSave;
    private Button _btnClear;
    private Button _btnSwitchToTextEntry;
    private DrawingCanvasView _drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        // Get buttons from view
        _btnSave = findViewById(R.id.btn_save);
        _btnClear = findViewById(R.id.btn_clear);
        _btnSwitchToTextEntry = findViewById(R.id.btn_switch_to_user_entry);
        _drawingView = findViewById(R.id.drawing_area);

        // Map buttons to functions
        _btnSwitchToTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartUserEntryActivity();
            }
        });

        _btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _drawingView.ClearCanvas();
            }
        });

        _btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCanvas();
            }
        });
    }

    /* Save the user created image */
    public void SaveCanvas()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Drawing");
            saveDialog.setMessage("Save drawing to device?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which){
                    // Generate a random file name and save the image as a PNG
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
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    public static Bitmap loadBitmapFromView(View view) {

        // width measure spec
        int widthSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        // height measure spec
        int heightSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredHeight(), View.MeasureSpec.AT_MOST);
        // measure the view
        view.measure(widthSpec, heightSpec);
        // set the layout sizes
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(), view.getMeasuredHeight() + view.getTop());
        // create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // create a canvas used to get the view's image and draw it on the bitmap
        Canvas c = new Canvas(bitmap);
        // position the image inside the canvas
        c.translate(-view.getScrollX(), -view.getScrollY());
        // get the canvas
        view.draw(c);

        return bitmap;
    }


    /* Switch back to the user entry activity */
    private void StartUserEntryActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
