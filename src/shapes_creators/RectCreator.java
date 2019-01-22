package shapes_creators;

import shapes.Rect;
import shapes.Shape;

public class RectCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Rect();
    }
}
