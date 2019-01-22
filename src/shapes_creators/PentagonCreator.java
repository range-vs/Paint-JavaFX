package shapes_creators;

import shapes.Pentagon;
import shapes.Shape;

public class PentagonCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Pentagon();
    }
}
