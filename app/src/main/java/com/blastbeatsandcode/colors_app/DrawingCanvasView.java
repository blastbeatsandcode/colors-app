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

/* Custom view for drawing on screen */
public class DrawingCanvasView extends View {
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private List<Button> buttons;



    public DrawingCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Set up drawing area
        PrepareDrawingArea();
    }

    /* Set up drawing area so users can interact with it */
    private void PrepareDrawingArea()
    {
        drawPath = new Path();
        drawPaint = new Paint();

        buttons = new ArrayList<Button>();

        // Set up initial paint settings
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    /* Adds a button to the button list to be grayed out when drawing */
    public void AddButton(Button button)
    {
        buttons.add(button);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            drawPath.moveTo(x, y);
            TransparentButtons(); // Make buttons transparent
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            drawPath.lineTo(x, y);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            drawCanvas.drawPath(drawPath, drawPaint);
            drawPath.reset();
            OpaqueButtons(); // Make buttons opaque again
        }
        invalidate();
        return true;
    }

    private void OpaqueButtons() {
        // When user is done drawing, return buttons to full alpha state
        if (!buttons.isEmpty()) {
            for (Button button : buttons) {
                button.setAlpha(1f);
            }
        }
    }

    private void TransparentButtons() {
        // Make buttons transparent while drawing
        if (!buttons.isEmpty()) {
            for (Button button : buttons) {
                button.setAlpha(0.25f);
            }
        }
    }


    /* Set attributes for paint */
    public void SetPaint(int color)
    {
        drawPaint.setColor(color);
    }

    /* Gets the current paint color */
    public int GetPaintColor()
    {
        return drawPaint.getColor();
    }

    /* Clears the canvas */
    public void ClearCanvas()
    {
        int h = drawCanvas.getHeight();
        int w = drawCanvas.getWidth();

        onSizeChanged(w, h, w, h);
        invalidate();
    }


}
