package com.example.androidventure.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.androidventure.R;

/**
 * Custom view that draws connectors between code blocks
 */
public class CodeConnector extends View {

    private Paint linePaint;
    private Paint arrowPaint;
    private Path path;
    private float startX, startY;
    private float endX, endY;
    private boolean drawArrow = true;
    private int lineColor;
    private float lineWidth = 6f;
    private boolean isDashed = false;
    private boolean isConnected = false;

    public CodeConnector(Context context) {
        super(context);
        init(context);
    }

    public CodeConnector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CodeConnector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Set up paint for the connector line
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setAntiAlias(true);
        lineColor = ContextCompat.getColor(context, R.color.connector_line);
        linePaint.setColor(lineColor);

        // Set up paint for arrow head
        arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(lineColor);

        // Initialize path
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (startX == 0 && startY == 0 && endX == 0 && endY == 0) {
            return; // Nothing to draw yet
        }

        // Draw the line from start to end
        if (isDashed) {
            // Create dashed effect for line
            linePaint.setPathEffect(new android.graphics.DashPathEffect(new float[]{15, 10}, 0));
        } else {
            linePaint.setPathEffect(null);
        }

        // Draw the connector line
        path.reset();
        path.moveTo(startX, startY);

        // Create a smooth curve for the connector
        float controlX1 = startX + (endX - startX) / 3;
        float controlY1 = startY;
        float controlX2 = endX - (endX - startX) / 3;
        float controlY2 = endY;

        path.cubicTo(controlX1, controlY1, controlX2, controlY2, endX, endY);
        canvas.drawPath(path, linePaint);

        // Draw the arrow at the end if needed
        if (drawArrow) {
            // Calculate the angle of the line at the end point
            float dx = endX - controlX2;
            float dy = endY - controlY2;
            float angle = (float) Math.atan2(dy, dx);

            // Draw the arrow head
            float arrowSize = 20f;
            Path arrowPath = new Path();
            arrowPath.moveTo(endX, endY);
            arrowPath.lineTo(
                    (float) (endX - arrowSize * Math.cos(angle - Math.PI / 6)),
                    (float) (endY - arrowSize * Math.sin(angle - Math.PI / 6))
            );
            arrowPath.lineTo(
                    (float) (endX - arrowSize * Math.cos(angle + Math.PI / 6)),
                    (float) (endY - arrowSize * Math.sin(angle + Math.PI / 6))
            );
            arrowPath.close();
            canvas.drawPath(arrowPath, arrowPaint);
        }
    }

    /**
     * Set the start point of the connector
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setStartPoint(float x, float y) {
        this.startX = x;
        this.startY = y;
        invalidate();
    }

    /**
     * Set the end point of the connector
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setEndPoint(float x, float y) {
        this.endX = x;
        this.endY = y;
        invalidate();
    }

    /**
     * Set connector points
     *
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     */
    public void setPoints(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        invalidate();
    }

    /**
     * Enable or disable arrow at the end of the connector
     *
     * @param draw Whether to draw the arrow
     */
    public void setDrawArrow(boolean draw) {
        this.drawArrow = draw;
        invalidate();
    }

    /**
     * Set the color of the connector
     *
     * @param color Color value
     */
    public void setLineColor(int color) {
        this.lineColor = color;
        linePaint.setColor(color);
        arrowPaint.setColor(color);
        invalidate();
    }

    /**
     * Set the width of the connector line
     *
     * @param width Line width
     */
    public void setLineWidth(float width) {
        this.lineWidth = width;
        linePaint.setStrokeWidth(width);
        invalidate();
    }

    /**
     * Set whether the line should be dashed
     *
     * @param dashed Whether the line should be dashed
     */
    public void setDashed(boolean dashed) {
        this.isDashed = dashed;
        invalidate();
    }

    /**
     * Set the connection status (changes the color)
     *
     * @param connected Whether the blocks are properly connected
     */
    public void setConnected(boolean connected) {
        this.isConnected = connected;
        if (connected) {
            setLineColor(ContextCompat.getColor(getContext(), R.color.success_green));
        } else {
            setLineColor(ContextCompat.getColor(getContext(), R.color.error_red));
        }
    }
}