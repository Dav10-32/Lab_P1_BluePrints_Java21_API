package edu.eci.arsw.blueprints.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Schema(description = "A coordinate point (x,y)")
public class Point implements Serializable {

    @Schema(description = "X coordinate", example = "10")
    private int x;
    @Schema(description = "Y coordinate", example = "20")
    private int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Compatibility methods for record-style access
    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
