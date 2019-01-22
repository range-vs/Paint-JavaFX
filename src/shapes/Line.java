package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rendering.IClonable;

import java.util.Arrays;

public class Line extends Shape {
    @Override
    public void render(GraphicsContext gr) {
        gr.setLineWidth(strokeWidth);
        gr.setStroke(strokeColor);
        gr.strokePolygon(x, y, x.length);
    }

    @Override
    public void constructShape(double x1, double y1, double x2, double y2, int w, Color sc, Color fc) {
        x = new double[]{x1, x2};
        y = new double[]{y1, y2};
        strokeWidth = w;
        strokeColor = sc;
    }

    @Override
    public Shape clones() {
        Line line = new Line();
        line.x = Arrays.copyOf(this.x, this.x.length);
        line.y = Arrays.copyOf(this.y, this.y.length);
        line.strokeColor = this.strokeColor;
        line.fillColor = this.fillColor;
        line.strokeWidth = this.strokeWidth;
        return line;
    }
}
