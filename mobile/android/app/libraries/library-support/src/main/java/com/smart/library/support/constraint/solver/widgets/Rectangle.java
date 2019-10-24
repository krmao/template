package com.smart.library.support.constraint.solver.widgets;

public class Rectangle
{
    public int x;
    public int y;
    public int width;
    public int height;
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    void grow(final int w, final int h) {
        this.x -= w;
        this.y -= h;
        this.width += 2 * w;
        this.height += 2 * h;
    }
    
    boolean intersects(final Rectangle bounds) {
        return this.x >= bounds.x && this.x < bounds.x + bounds.width && this.y >= bounds.y && this.y < bounds.y + bounds.height;
    }
    
    public boolean contains(final int x, final int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }
    
    public int getCenterX() {
        return (this.x + this.width) / 2;
    }
    
    public int getCenterY() {
        return (this.y + this.height) / 2;
    }
}
