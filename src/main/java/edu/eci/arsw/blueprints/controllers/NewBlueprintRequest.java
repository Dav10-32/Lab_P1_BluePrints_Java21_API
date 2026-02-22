package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Point;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Request DTO for creating a new blueprint.
 */
public record NewBlueprintRequest(
        @NotBlank String author,
        @NotBlank String name,
        @Valid List<Point> points) {
}
