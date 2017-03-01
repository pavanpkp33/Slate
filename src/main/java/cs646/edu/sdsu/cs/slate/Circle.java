package cs646.edu.sdsu.cs.slate;

import android.graphics.Paint;

/**
 * Created by Pkp on 2/22/2017.
 */

/*
 Reference: Text : Introduction to Android Programming - Chapter : 7
 */

public class Circle {

    private float centerX;
    private float centerY;
    private float circleRadius;
    private Paint colorSelected;
    private double velocityX;
    private double velocityY;
    private int REVERSE  = -1;
    private double FRICTION = 0.90;
    private float left, right, top, bottom;

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public int getREVERSE() {
        return REVERSE;
    }

    public void setREVERSE(int REVERSE) {
        this.REVERSE = REVERSE;
    }

    public double getFRICTION() {
        return FRICTION;
    }

    public void setFRICTION(double FRICTION) {
        this.FRICTION = FRICTION;
    }

    public Circle(){

    }

    public Circle(float centerX, float centerY, float circleRadius, Paint colorSelected) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.circleRadius = circleRadius;
        this.colorSelected = colorSelected;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
    }

    public Paint getColorSelected() {
        return colorSelected;
    }

    public void setColorSelected(Paint colorSelected) {
        this.colorSelected = colorSelected;
    }
    /*
    Method to move the circle.
    Logic : Assign the normalized velocity to the circle's center, and reduce the velocity
    by introducing a friction.
     */
    public void move(){

        centerX += velocityX;
        centerY += velocityY;

        velocityX *= FRICTION;
        velocityY *= FRICTION;

        if(Math.abs(velocityX) < 1){
            velocityX = 0;
        }

        if(Math.abs(velocityY) < 1){
            velocityY = 0;

        }

        checkCollision();

    }

    public void setCollisionBoundaries(int l, int r, int t, int b){

        left = l+circleRadius;
        right = r-circleRadius;
        top = t+circleRadius;
        bottom = b-circleRadius;
    }
    //Method to check boundaries, If there is a collision, reverse the direction.
    private void checkCollision() {

        if(centerX < left){
            centerX = left;
            velocityX *= REVERSE;
        }else if(centerX > right){
            centerX = right;
            velocityX *= REVERSE;
        }

        if(centerY < top){
            centerY = top;
            velocityY *= REVERSE;
        }else if(centerY > bottom){
            centerY = bottom;
            velocityY *= REVERSE;
        }
    }
}
