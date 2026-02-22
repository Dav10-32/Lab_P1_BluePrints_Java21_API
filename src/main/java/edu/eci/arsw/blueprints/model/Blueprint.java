package edu.eci.arsw.blueprints.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "blueprints")
@IdClass(BlueprintId.class)
public class Blueprint {

    @Id
    @Schema(description = "The author of the blueprint", example = "john")
    private String author;

    @Id
    @Schema(description = "The unique name of the blueprint for this author", example = "house")
    private String name;

    @ElementCollection
    @Schema(description = "List of points that form the blueprint")
    private List<Point> points;

    public Blueprint() {
        this.points = new ArrayList<>();
    }

    public Blueprint(String author, String name, List<Point> pts) {
        this.author = author;
        this.name = name;
        this.points = new ArrayList<>();
        if (pts != null) {
            this.points.addAll(pts);
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Blueprint bp))
            return false;
        return Objects.equals(author, bp.author) && Objects.equals(name, bp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }

    @Override
    public String toString() {
        return "Blueprint{" + "author='" + author + '\'' + ", name='" + name + '\'' + ", points=" + points + '}';
    }
}
