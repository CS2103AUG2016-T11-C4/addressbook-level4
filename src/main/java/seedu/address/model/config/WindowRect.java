package seedu.address.model.config;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;

/**
 * Stores a UI Window's dimensions.
 */
public class WindowRect {
    private static final double DEFAULT_WIDTH = 600;
    private static final double DEFAULT_HEIGHT = 740;

    private final double width;
    private final double height;
    private final Optional<Double> x;
    private final Optional<Double> y;

    public WindowRect(double width, double height, Optional<Double> x, Optional<Double> y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public WindowRect(double width, double height, double x, double y) {
        this(width, height, Optional.of(x), Optional.of(y));
    }

    public WindowRect(double width, double height) {
        this(width, height, Optional.empty(), Optional.empty());
    }

    public WindowRect() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Optional<Double> getX() {
        return x;
    }

    public Optional<Double> getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
               || (other instanceof WindowRect
               && width == ((WindowRect)other).width
               && height == ((WindowRect)other).height
               && x.equals(((WindowRect)other).x)
               && y.equals(((WindowRect)other).y)
               );
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, x, y);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("width", width)
                .add("height", height)
                .add("x", x)
                .add("y", y)
                .toString();
    }
}
