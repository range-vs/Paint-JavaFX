package shapes_creators;

import shapes.Line;
import shapes.Shape;

public class LineCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Line();
    }
}
