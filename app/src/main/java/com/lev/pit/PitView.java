package com.lev.pit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Lev on 18/04/2017.
 */
public class PitView extends View {
    public int width;
    public int height;
    Context context;
    private static final float TOLERANCE = 50;
    private Point movingPoint;

    private Point center;
    private ArrayList<Point> mPoints;

    public PitView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mPoints = new ArrayList<Point>();
        movingPoint = null;
        center = new Point();
        context = c;


    }

    public PitView(Context context) {
        super(context);
    }


    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

         width = w;
         height = h;
         center.set(w/2,h/2);

        //create some random points for the start
        if(mPoints!= null && mPoints.size() == 0)
        for(int i =0 ;i<5;i++) {
            Point p = new Point();
            Random r = new Random();
            int x = r.nextInt((w - 20) - 20) + 20;
            int y = r.nextInt((h - 20) - 20) + 20;
            p.set(x,y);
            mPoints.add(p);
        }

        invalidate();
    }

    private void createRandomPoints(){



    }

    //draw the x and y axis
    private void drawXYAxis(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#87CEFA"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        //yAxis
        canvas.drawLine(center.x,0 ,center.x, height, paint);
        // xAxis
        canvas.drawLine(0,center.y , width, center.y, paint);
    }

    //draw an edge
    private void drawPoint(Canvas canvas ,Point point){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x,point.y ,10f, paint);

    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawXYAxis(canvas);
        drawAllPoints(canvas);
        drawLineBetweenPoints(canvas);

    }

    //draw lines between all the edges in ascending order , so we switch between some of edges
    private void drawLineBetweenPoints(Canvas canvas){

        Collections.sort(mPoints, new PointCompare());
        Path path = new Path();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);

        path.moveTo(mPoints.get(0).x,mPoints.get(0).y);

        for(int i = 1;i<mPoints.size();i++){
            path.lineTo(mPoints.get(i).x,mPoints.get(i).y);
        }

        canvas.drawPath(path, paint);

    }

    //draw all points
    private void drawAllPoints(Canvas canvas){
        for (Point p : mPoints) {
            drawPoint(canvas,p);
        }
    }


    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {

        //check if we touched one of the edges
        for (Point p : mPoints) {

            float dx = Math.abs(p.x - x);
            float dy = Math.abs(p.y - y);

            if (dx <= TOLERANCE && dy <= TOLERANCE) {
                //if we did touch ,get the edge
                movingPoint = p;
                break;
            }

        }


    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        if(movingPoint!=null)
            movingPoint.set((int)x,(int)y);
    }

    // add new edge in the center of the view
    public void addPoint(){
        Point point = new Point(center.x,center.y);
        mPoints.add(point);
        invalidate();

    }

    // when ACTION_UP stop touch
    private void upTouch() {
        movingPoint = null;
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }

     //we need comparator inorder to sort edges by x values and than draw lines between them by ascending order
     class PointCompare implements Comparator<Point> {
        public int compare(Point a, Point b) {
            if (a.x < b.x) {
                return -1;
            }
            else if (a.x > b.x) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
