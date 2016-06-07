/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myimagecapture;

/**
 *
 * @author Frederik
 */
public class RGB {
    int red;
    int green;
    int blue;
    int x;
    int y;

    public void printColors(){
        System.out.println("Red: " + red + " Green: " + green + " Blue: " + blue);
    }    
    
    public RGB(int red, int gree, int blue, int x, int y) {
        this.red = red;
        this.green = gree;
        this.blue = blue;
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGree() {
        return green;
    }

    public void setGree(int gree) {
        this.green = gree;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
    
    
}
