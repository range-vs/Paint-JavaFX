package shapes_creators;

import shapes.Shape;
import shapes.Triangle;

public class TriangleCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Triangle();
    }
}
