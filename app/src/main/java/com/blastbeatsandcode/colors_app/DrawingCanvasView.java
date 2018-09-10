package com.blastbeatsandcode.colors_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/*
 *  Custom view for drawing on screen.
 *  This custom view was made possible with help from this excellent article by Sue Smith:
 *  https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202
 * */
public class DrawingCanvasView extends View {
    // Member variables
    private Path _drawingPath;          // Holds the path the user draws
    private Paint _drawingPaint;        // Current paint settings which will be set by the user
    private Paint _canvasPaint;         // The paint on the canvas
    private Canvas _drawingCanvas;      // The canvas we will be extending to use as a drawing surface
    private Bitmap _canvasBitmap;       // Bitmap the user will be drawing on/creating
    private List<Button> buttons;       // Optional list of buttons which will become transparent when user is drawing

    /*
    *   Drawing canvas constructor which initializes the brush and drawing area.
    *   This also initializes the buttons list which can be added to by using AddButton().
    * */
    public DrawingCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        PrepareDrawingArea();
    }

    /*
    *   Set up drawing area so users can interact with it
    * */
    private void PrepareDrawingArea()
    {
        _drawingPath = new Path();
        _drawingPaint = new Paint();

        buttons = new ArrayList<Button>();

        // Set up initial paint settings
        _drawingPaint.setColor(Color.BLACK);
        _drawingPaint.setAntiAlias(true);
        _drawingPaint.setStrokeWidth(20);
        _drawingPaint.setStyle(Paint.Style.STROKE);
        _drawingPaint.setStrokeJoin(Paint.Join.ROUND);
        _drawingPaint.setStrokeCap(Paint.Cap.ROUND);

        _canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    /*
    *   Adds a button to the button list to be made transparent when user is drawing.
    * */
    public void AddButton(Button button)
    {
        buttons.add(button);
    }

    /*
    *   Executes whenever the size of the canvas is changed (which is when it is initialized)
    *   Or when we call it; it is used to clear the canvas when the user presses the "clear" button.
    * */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        // Creates the bitmap and a new canvas to draw upon
        _canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _drawingCanvas = new Canvas(_canvasBitmap);
    }

    /*
    *   Executes whenever the canvas is drawn.
    *   The bitmap is added to here and the path is drawn with the specified path and paint settings.
    * */
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(_canvasBitmap, 0, 0, _canvasPaint);
        canvas.drawPath(_drawingPath, _drawingPaint);
    }

    /*
    *   Executes when a touch event is triggered on the drawing canvas view.
    *   This is how the drawn paths are actually added to the canvas view.
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();                                     // Store touch coordinates
        float y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){           // Check for initial press
            _drawingPath.moveTo(x, y);                              // Start the new path here
            TransparentButtons();                                   // Make buttons transparent
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){     // Check if drawing path
            _drawingPath.lineTo(x, y);                              // Draw path to this location
        }else if(event.getAction() == MotionEvent.ACTION_UP){       // Check if drawing finished
            _drawingCanvas.drawPath(_drawingPath, _drawingPaint);   // Add the full path to the canvas
            _drawingPath.reset();                                   // Reset so we can start a new path
            OpaqueButtons();                                        // Make buttons opaque again
        }

        // invalidate() is called to get the next touch event and scrap the current one
        invalidate();
        return true;
    }

    /*
    * If buttons list is not empty, sets all of the buttons in the buttons list to alpha 1.0.
    * Called after the user is done drawing a path.
    * */
    private void OpaqueButtons() {
        if (!buttons.isEmpty()) {
            for (Button button : buttons) {
                button.setAlpha(1f);
            }
        }
    }

    /*
     * If buttons list is not empty, sets all of the buttons in the buttons list to transparent.
     * Called when the user starts drawing on the canvas.
     * */
    private void TransparentButtons() {
        // Make buttons transparent while drawing
        if (!buttons.isEmpty()) {
            for (Button button : buttons) {
                button.setAlpha(0.25f);
            }
        }
    }


    /*
    *   Set attributes for paint when user selects it in the color picker.
    * */
    public void SetPaint(int color)
    {
        _drawingPaint.setColor(color);
    }

    /*
    *   Gets the current paint color.
    * */
    public int GetPaintColor()
    {
        return _drawingPaint.getColor();
    }

    /*
    *   Clears the canvas by calling onSizeChanged and invalidating the view.
    * */
    public void ClearCanvas()
    {
        int h = _drawingCanvas.getHeight();
        int w = _drawingCanvas.getWidth();

        onSizeChanged(w, h, w, h);
        invalidate();
    }
}
