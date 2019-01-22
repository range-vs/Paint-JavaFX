package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rendering.IClonable;

import java.util.Arrays;

public class Hexagon extends Shape {
    private boolean isRotate = false;
    private double centerX = 0;
    private double centerY = 0;

    @Override
    public void render(GraphicsContext gr) {
        if(isRotate){
            gr.save();
            gr.translate(centerX, centerY);
            gr.rotate(180);
            gr.translate(-centerX, -centerY);
        }
        gr.setFill(fillColor);
        gr.fillPolygon(x, y, x.length);
        gr.setLineWidth(strokeWidth);
        gr.setStroke(strokeColor);
        gr.strokePolygon(x, y, x.length);
        gr.restore();
    }

    @Override
    public void constructShape(double x1, double y1, double x2, double y2, int w, Color sc, Color fc) {
        x = new double[]{x1, x2}; Arrays.sort(x);
        y = new double[]{y1, y2}; Arrays.sort(y);
        if(y2 < y1) {
            isRotate = true;
            centerX = (x1 + x2) / 2;
            centerY = (y1 + y2) / 2;
        }
        x = new double[]{x[0] + (x[1] - x[0])/4, x[1] - (x[1] - x[0])/4, x[1], x[1],  x[1] - (x[1] - x[0])/4, x[0] + (x[1] - x[0])/4, x[0], x[0], x[0] + (x[1] - x[0])/4, x[1] - (x[1] - x[0])/4};
        y =new double[] {y[0], y[0], y[0] + (y[1] - y[0])/4, y[1] - (y[1] - y[0])/4, y[1], y[1],y[1] - (y[1] - y[0])/4, y[0] + (y[1] - y[0])/4, y[0], y[0]};
        strokeWidth = w;
        strokeColor = sc;
        fillColor = fc;
    }

    @Override
    public Shape clones() {
        Hexagon hex = new Hexagon();
        hex.centerX = this.centerX;
        hex.centerY = this.centerX;
        hex.isRotate = this.isRotate;
        hex.x = Arrays.copyOf(this.x, this.x.length);
        hex.y = Arrays.copyOf(this.y, this.y.length);
        hex.strokeColor = this.strokeColor;
        hex.fillColor = this.fillColor;
        hex.strokeWidth = this.strokeWidth;
        return hex;
    }
}
