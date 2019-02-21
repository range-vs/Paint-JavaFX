package rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import shapes.*;
import shapes_creators.*;

import java.awt.Point;
import java.util.Stack;

public class Render {
    private boolean isInit = false;
    private int strokeWidth;
    private Color strokeColor;
    private Color fillColor;
    private Image snapshot;
    private int canvasWidth;
    private int canvasHeight;

    public static Render render = null;

    private Render(){}

    public static Render getInstanced(){
        if(render == null)
            render = new Render();
        return render;
    }

    public void Init(int sw, Color sc, Color fc) {
        this.strokeWidth = sw;
        this.strokeColor = sc;
        this.fillColor = fc;
        canvasWidth = canvasHeight = 0;
        isInit = true;
    }

    public void floodFill(int _x, int _y, Image img, int w, int h){
        if(!isInit)
            return;
        Stack<Point> st = new Stack<Point>();
        Point l[] = new Point[] {new Point(1,0), new Point(0,1), new Point(-1,0), new Point(0,-1),
                /*new Point(1,-1), new Point(1,1), new Point(-1,1), new Point(-1,-1)*/};
        Color back = null;
        back = img.getPixelReader().getColor(_x, _y);
        if(back.equals(fillColor))
            return;

        Point p = new Point(_x, _y);
        do {
            for(int i = 0;i<l.length;i++) {
                int x = p.x + l[i].x;
                int y = p.y + l[i].y;
                if(x < 0 || y < 0 || x >= w || y >= h)
                    continue;
                Color pixel = img.getPixelReader().getColor(x, y);
                if(pixel.equals(back)) {
                    ((WritableImage) img).getPixelWriter().setColor(x, y, fillColor);
                    st.push(new Point(x, y));
                }
            }
            if(st.size() != 0)
                p = st.pop();
            else
                break;
        }while(true);
    }

    public void reflash(Image img, GraphicsContext gr){
        gr.drawImage(img, 0,0);
    }

    public void draw(int type, double x1, double y1, double x2, double y2, GraphicsContext gr){
        if(!isInit)
            return;
        ShapeCreator shapeCreator = null;
        switch(type) {
            case 0:
                shapeCreator = new RectCreator();
                break;

            case 1:
                shapeCreator = new SphereCreator();
                break;

            case 2:
                shapeCreator = new LineCreator();
                break;

            case 3:
                shapeCreator = new TriangleCreator();
                break;

            case 4:
                shapeCreator = new PentagonCreator();
                break;

            case 5:
                shapeCreator = new HexagonCreator();
                break;

            default:
                break;
        }
        Shape r = shapeCreator.create();
        r.constructShape(x1, y1, x2, y2, strokeWidth, strokeColor, fillColor);
        r.render(gr);
    }

    public void setFillColor(Color fc){
        fillColor = fc;
    }

    public void setStrokeColor(Color sc){
        strokeColor = sc;
    }

    public void setStrokeWidth(int w)
    {
        strokeWidth = w;
    }

    public void startPath(int x1, int y1, GraphicsContext gr){
        if(!isInit)
            return;
        startPath(strokeColor, x1, y1, gr);
    }

    public void startPath(Color cl, int x1, int y1, GraphicsContext gr){
        if(!isInit)
            return;
        gr.beginPath();
        gr.setLineCap(StrokeLineCap.ROUND);
        gr.setLineJoin(StrokeLineJoin.ROUND);
        gr.setStroke(cl);
        gr.setLineWidth(strokeWidth);
        //gr.lineTo(x1, y1);
        gr.lineTo(x1, y1);

        gr.stroke();


        /*gr.setFill(cl);
        gr.fillOval(x1, y1, strokeWidth, strokeWidth);*/
    }

    public void addPointPath(int x2, int y2, GraphicsContext gr){
        if(!isInit)
            return;
        gr.lineTo(x2, y2);
        gr.stroke();
    }

    public void endPath(GraphicsContext gr){
        if(!isInit)
            return;
        gr.closePath();
    }

    public int getStrokeWidth(){
        return strokeWidth;
    }
}
