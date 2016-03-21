package org.hci.doodler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by damiennagle on 3/9/16.
 */
public class DoodleView extends View {

    private static float paintBrushMax = 20.0f;
    private Paint paintDoodle = new Paint();

    private HashMap<Path,Paint> drawnPaths = new HashMap<Path,Paint>();
    private LinkedList<Path> pathOrder = new LinkedList<Path>();

    private Path currentPath;
    private Paint currentPaint;

    private float currentStrokeWidth;

    /* Paint color variables */
    private int currentOpacity;
    private int currR;
    private int currB;
    private int currG;

    public DoodleView(Context context) {
        super(context);
        init(null,0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public DoodleView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle){
        /* Initialize current variables */
        this.currentStrokeWidth = 6.0f;
        this.currentOpacity = 255;

        /* Default color is black */
        this.currR = 0;
        this.currB = 0;
        this.currG = 0;

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        /* Iterate over the map and draw all the paths with their paints */
        for (Path p : drawnPaths.keySet()) {
            Paint currPaint = drawnPaths.get(p);
            canvas.drawPath(p, currPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()){
            /* This is going to create a new path */
            case MotionEvent.ACTION_DOWN:
                this.downPath(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                this.currentPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }

    /**
     * This will initialize a new path at this location,
     * or continue the current path if the paint parameters haven't changed
     * @param touchX
     * @param touchY
     */
    private void downPath(float touchX, float touchY) {
        if (changedParameters()) {

            /* If the paint parameters have changed we must make a new path */
            Path newPath = new Path();
            newPath.moveTo(touchX, touchY);

            /* Create a new paint from the parameters */
            Paint newPaint = createPaint();

            /* Save the path */
            this.pathOrder.add(newPath);
            this.drawnPaths.put(newPath, newPaint);

            /* Set the new path and color */
            this.currentPath = newPath;
            this.currentPaint = newPaint;

        } else {

            /* Continue the current path at a new place */
            this.currentPath.moveTo(touchX,touchY);

        }
    }

    private boolean changedParameters() {
        /* If current paint is null reinitialize it */
        if (currentPaint == null || currentPath == null) {
            return true;
        }

        /* Check if any of the parameters are different from the current paint type */
        if (currentPaint.getColor() != Color.argb(this.currentOpacity,this.currR,this.currG,this.currB)) {
            return true;
        }

        /* Not sure if we need error correction for floats */
        if (Math.abs(currentPaint.getStrokeWidth() - currentStrokeWidth) > 0.05f ) {
            return true;
        }

        return false;
    }

    private Paint createPaint() {
        Paint newPaint = new Paint();
        /* Create a color from current values */
        newPaint.setStrokeWidth(this.currentStrokeWidth);
        newPaint.setColor(Color.argb(this.currentOpacity,this.currR,this.currG,this.currB));
        newPaint.setStyle(Paint.Style.STROKE);
        newPaint.setAntiAlias(true);

        return newPaint;
    }

    /**
     * Takes in a size from 0 - 100 and sets the tooltip to that size scaled to the min and max of the drawing tool
     * @param size
     */
    public void setToolTipSize(int size) {
        float percentage = (float) (size + 1) / 10.0f;
        currentStrokeWidth = percentage * paintBrushMax;
    }

    /**
     * The bar that controls the opacity goes from 0 - 255 so no need to convert
     * @param opacity
     */
    public void setToolTipOpacity(int opacity) {
        currentOpacity = opacity;
    }

    /**
     * This should clear all of the
     */
    public void clearScreen() {
        /* Set all the paints as white */
        for(Path p : drawnPaths.keySet()) {
            drawnPaths.get(p).setColor(Color.WHITE);
        }

        /* Force a repaint */
        invalidate();

        /* Remove the old paths */
        drawnPaths = new HashMap<Path,Paint>();
    }

    /* This updates the value for the color when a button has been pressed */
    public void updateColor(String buttonVal) {
        System.out.println("Updating value to: " + buttonVal);

        switch (buttonVal) {
            case "Red":
                this.currR = 255;
                this.currG = 0;
                this.currB = 0;
                break;
            case "Green":
                this.currR = 0;
                this.currG = 255;
                this.currB = 0;
                break;
            case "Blue":
                this.currR = 0;
                this.currG = 0;
                this.currB = 255;
                break;
            case "Black":
                this.currR = 0;
                this.currG = 0;
                this.currB = 0;
                break;
            case "Clear Screen":
                this.clearScreen();
                break;
        }
    }

}
