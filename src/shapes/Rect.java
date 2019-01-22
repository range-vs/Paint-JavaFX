package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rendering.IClonable;

import java.util.Arrays;

public class Rect extends Shape{

    @Override
    public void render(GraphicsContext gr) {
        gr.setFill(fillColor);
        gr.fillPolygon(x, y, x.length);
        gr.setLineWidth(strokeWidth);
        gr.setStroke(strokeColor);
        gr.strokePolygon(x, y, x.length);
    }

    @Override
    public void constructShape(double x1, double y1, double x2, double y2, int w, Color sc, Color fc) {
        x = new double[]{x1, x2, x2, x1, x1};
        y = new double[]{y1, y1, y2, y2, y1};
        strokeWidth = w;
        strokeColor = sc;
        fillColor = fc;
    }

    @Override
    public Shape clones() {
        Rect rect = new Rect();
        rect.x = Arrays.copyOf(this.x, this.x.length);
        rect.y = Arrays.copyOf(this.y, this.y.length);
        rect.strokeColor = this.strokeColor;
        rect.fillColor = this.fillColor;
        rect.strokeWidth = this.strokeWidth;
        return rect;
    }
}
