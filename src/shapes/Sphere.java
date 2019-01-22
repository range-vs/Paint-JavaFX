package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Sphere extends Shape{

    @Override
    public void render(GraphicsContext gr) {
        gr.setFill(fillColor);
        gr.fillOval(x[0], y[0], x[1] - x[0], y[1] - y[0]);
        gr.setLineWidth(strokeWidth);
        gr.setStroke(strokeColor);
        gr.strokeOval(x[0], y[0], x[1] - x[0], y[1] - y[0]);
    }

    @Override
    public void constructShape(double x1, double y1, double x2, double y2, int w, Color sc, Color fc) {
        x = new double[]{x1, x2}; Arrays.sort(x);
        y = new double[]{y1, y2}; Arrays.sort(y);
        strokeWidth = w;
        strokeColor = sc;
        fillColor = fc;
    }

    @Override
    public Shape clones() {
        Sphere shp = new Sphere();
        shp.x = Arrays.copyOf(this.x, this.x.length);
        shp.y = Arrays.copyOf(this.y, this.y.length);
        shp.strokeColor = this.strokeColor;
        shp.fillColor = this.fillColor;
        shp.strokeWidth = this.strokeWidth;
        return shp;
    }
}
