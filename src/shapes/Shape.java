package shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rendering.IClonable;

public abstract class Shape implements IClonable {
    protected int strokeWidth;
    protected Color strokeColor;
    protected Color fillColor;
    protected double []x;
    protected double []y;

    abstract public void render(GraphicsContext gr); // вызываем отрисовку
    abstract public void constructShape(double x1, double y1, double x2, double y2, int w, Color sc, Color fc); // конструируем новую фигуру

}
