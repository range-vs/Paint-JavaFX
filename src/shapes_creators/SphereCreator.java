package shapes_creators;

import shapes.Shape;
import shapes.Sphere;

public class SphereCreator extends ShapeCreator {
    @Override
    public Shape create() {
        return new Sphere();
    }
}
