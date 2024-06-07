package org.example.stages;

import javafx.scene.canvas.Canvas;

public class StickmanWrapper {
    private final Canvas canvas;
    private double dx;
    private double dy;

    public StickmanWrapper(Canvas canvas, double dx, double dy) {
        this.canvas = canvas;
        this.dx = dx;
        this.dy = dy;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }
}