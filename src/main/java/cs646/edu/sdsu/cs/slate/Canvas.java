package cs646.edu.sdsu.cs.slate;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


/**
 * Created by Pkp on 2/19/2017.
 */

public class Canvas extends View implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private int viewWidth, viewHeight;
    private float circleStartX, circleStartY, circleCurrentX, circleCurrentY, circleRadius;
    private final float defaultRadius = 15;
    private Paint circlePaint;

    public ArrayList<Circle>  circleList = new ArrayList<>();
    private ArrayList<Circle> onDownTouchedCircles, onUpTouchedCircles, commonCircles;
    GestureDetector gestureDetector;

    MainActivity objMain;


    public Canvas(Context context, AttributeSet attrs) {

        super(context, attrs);

        objMain = (MainActivity)getContext();
        gestureDetector = new GestureDetector(context, this);
        onUpTouchedCircles = new ArrayList<>();
        onDownTouchedCircles = new ArrayList<>();
        setPaint();
        setOnTouchListener(this);



}

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        for(Circle c : getCircleList()){
            float X = c.getCenterX();
            float Y = c.getCenterY();
            float radius = c.getCircleRadius();
            Paint paint = c.getColorSelected();
            canvas.drawCircle(X,Y,radius,paint);

        }
        if(objMain.getModeSelected().equals(MainActivity.MODE_DRAW)){
            circleRadius = getRadius(circleStartX, circleStartY, circleCurrentX, circleCurrentY);
            canvas.drawCircle(circleStartX,circleStartY, circleRadius, setPaint());
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //If the mode is Move, pass event to GestureDetector
        if(objMain.getModeSelected().equals(MainActivity.MODE_MOVE)){
            gestureDetector.onTouchEvent(event);
        }else{
            int action = event.getAction();
            int actionCode = action & MotionEvent.ACTION_MASK;
            switch(actionCode){

                case MotionEvent.ACTION_DOWN:
                    if(objMain.getModeSelected().equals(MainActivity.MODE_DRAW)){
                        return handleDownEventDraw(event);
                    }else if(objMain.getModeSelected().equals(MainActivity.MODE_DELETE)){
                        return handleDownEventDelete(event);
                    }else{
                        return handleDownEventMove(event);
                    }


                case MotionEvent.ACTION_MOVE:
                    if(objMain.getModeSelected().equals(MainActivity.MODE_DRAW)){
                        return handleMoveEventDraw(event);
                    }else if(objMain.getModeSelected().equals(MainActivity.MODE_DELETE)){
                        //do nothing
                        return true;
                    }else{
                        return handleMoveEventMove(event);
                    }


                case MotionEvent.ACTION_UP:
                    if(objMain.getModeSelected().equals(MainActivity.MODE_DRAW)) {
                        return handleUpEventDraw(event);
                    }else if(objMain.getModeSelected().equals(MainActivity.MODE_DELETE)) {
                        return handleUpEventDelete(event);
                    }else{
                        return handleUpEventMove(event);
                    }

            }
        }


        return true;
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    //Method to reset the circle
    public void resetCircle(float X, float Y, float R, Paint p){
        this.circleRadius = R;
        this.circleStartX = X;
        this.circleStartY = Y;
        this.circlePaint = p;
    }

    //Method to add circle to the list and set boundaries
    private boolean handleUpEventDraw(MotionEvent event) {
        circleCurrentX = event.getX();
        circleCurrentY = event.getY();
        Circle circle = new Circle(circleStartX, circleStartY, circleRadius, setPaint());
        circle.setVelocityX(0);
        circle.setVelocityY(0);
        circle.setCollisionBoundaries(5, viewWidth, 5, viewHeight);
        circleList.add(circle);
        invalidate();
        return true;
    }

    // Method to handle move touch event
private boolean handleMoveEventDraw(MotionEvent event) {
        circleCurrentX = event.getX();
        circleCurrentY = event.getY();
        invalidate();
        return true;
    }
    // Method to set circle's center
    private boolean handleDownEventDraw(MotionEvent event) {

        circleStartX = event.getX();
        circleStartY = event.getY();
        circleCurrentX = circleStartX;
        circleCurrentY = circleStartY;
        invalidate();

        return true;
    }

    private boolean handleUpEventMove(MotionEvent event) {

        return false;
    }
// Method to get touched circles and delete from array list
    private boolean handleUpEventDelete(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        commonCircles = new ArrayList<>();
        onUpTouchedCircles = getTouchedCircles(X,Y);
        commonCircles = intersection(onUpTouchedCircles, onDownTouchedCircles);
        for(Circle c : commonCircles){
            if(circleList.contains(c)){
                circleList.remove(c);

            }
        }
        invalidate();
        return true;
    }
    // Method to get circles which exists in both Array lists.
    private ArrayList<Circle> intersection(ArrayList<Circle> listA, ArrayList<Circle> listB) {
        ArrayList<Circle> commonCircles = new ArrayList<>();
        for(Circle c : listA){
            if(listB.contains(c)) {
                commonCircles.add(c);
            }
        }
        return commonCircles;
    }



    private boolean handleMoveEventMove(MotionEvent event) {
        return true;
    }



    private boolean handleDownEventMove(MotionEvent event) {

        return true;
    }

    private boolean handleDownEventDelete(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        onDownTouchedCircles = getTouchedCircles(X,Y);
        return true;
    }
//Method to get touched circles. Takes current X and Y co ordinates as params
    private ArrayList<Circle> getTouchedCircles(float x, float y) {
        ArrayList<Circle> touchList = new ArrayList<>();
        float circleCenterX, circleCenterY, circleRadius;
       if(circleList.size() > 0){
            for(Circle idx : circleList){
                circleCenterX = idx.getCenterX();
                circleCenterY = idx.getCenterY();
                circleRadius = idx.getCircleRadius();
                float xDist = x - circleCenterX;
                float yDist = y - circleCenterY;
                float distance = (float)Math.pow(xDist,2) + (float)Math.pow(yDist,2);
                if(distance <= (float)Math.pow(circleRadius,2)){
                    touchList.add(idx);
                }

            }
       }

        return touchList;
    }
    //method to set Paint object
    private Paint setPaint(){

        circlePaint = new Paint();
        circlePaint.setColor(objMain.getBrushColor());
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(8.0f);

        return circlePaint;
    }
//Method to get width and height of View.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
    }
    //Method which returns the radius of circle. Uses distance formula.
    private float getRadius(float startX, float startY, float currentX, float currentY){

        float radius, X, Y;
        float distanceLeftEdge, distanceRightEdge, distanceTopEdge, distanceBottomEdge;
        int maxWidth = viewWidth - 5;
        int maxHeight = viewHeight - 5;
        boolean leftEdge, rightEdge, topEdge, bottomEdge;
        distanceLeftEdge = startX - 5;
        distanceRightEdge = maxWidth - startX;
        distanceTopEdge = startY - 5;
        distanceBottomEdge = maxHeight - startY;
        X = currentX - startX;
        Y = currentY - startY;
        float currentRadius = (float)Math.sqrt((X*X) + (Y*Y));
        leftEdge = currentRadius > distanceLeftEdge;
        rightEdge = currentRadius > distanceRightEdge;
        topEdge = currentRadius > distanceTopEdge;
        bottomEdge = currentRadius > distanceBottomEdge;

        if(leftEdge || rightEdge || topEdge || bottomEdge) {
            radius = Math.min(Math.min(distanceLeftEdge,distanceRightEdge),Math.min(distanceTopEdge,distanceBottomEdge));
        } else if(currentRadius < 10){
            radius = defaultRadius;
        } else{
            radius = currentRadius;
        }
        return radius;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
//Method to move the circle
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        final float ADJUST = 0.025f; //Reduce velocity factor
        Circle editCircle;
        ArrayList<Circle>touchedCircle = new ArrayList<>();
        float x = e1.getX();
        float y = e1.getY();
        touchedCircle =  getTouchedCircles(x, y);
        for(Circle c : touchedCircle){

            if(circleList.contains(c)){
                editCircle = circleList.get(circleList.indexOf(c));
                editCircle.setVelocityX(velocityX * ADJUST);
                editCircle.setVelocityY(velocityY * ADJUST);
            }
        }
        invalidate();
        return true;
    }



}

