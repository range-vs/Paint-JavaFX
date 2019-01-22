package shapes_creators;

import shapes.Hexagon;
import shapes.Shape;

public class HexagonCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Hexagon();
    }
}
