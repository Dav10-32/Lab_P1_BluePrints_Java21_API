package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/blueprints")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Blueprints", description = "API for managing architectural blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) {
        this.services = services;
    }

    @GetMapping
    @Operation(summary = "Get all blueprints", description = "Retrieves a list of all blueprints in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blueprints retrieved successfully", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Set<Blueprint>>> getAll() {
        Set<Blueprint> blueprints = services.getAllBlueprints();
        return ResponseEntity.ok(new ApiResponse<>(200, "Blueprints retrieved successfully", blueprints));
    }

    @GetMapping("/{author}")
    @Operation(summary = "Get blueprints by author", description = "Retrieves all blueprints belonging to a specific author")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Author blueprints retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Author not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<?>> byAuthor(
            @Parameter(description = "The name of the author", required = true) @PathVariable String author) {
        try {
            Set<Blueprint> blueprints = services.getBlueprintsByAuthor(author);
            return ResponseEntity.ok(new ApiResponse<>(200, "Author blueprints retrieved", blueprints));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @GetMapping("/{author}/{bpname}")
    @Operation(summary = "Get specific blueprint", description = "Retrieves a specific blueprint by author and name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Blueprint found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blueprint not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<?>> byAuthorAndName(
            @Parameter(description = "The name of the author", required = true) @PathVariable String author,
            @Parameter(description = "The name of the blueprint", required = true) @PathVariable String bpname) {
        try {
            Blueprint bp = services.getBlueprint(author, bpname);
            return ResponseEntity.ok(new ApiResponse<>(200, "Blueprint found", bp));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @PostMapping
    @Operation(summary = "Create new blueprint", description = "Adds a new blueprint to the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Blueprint created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Blueprint already exists", content = @Content)
    })
    public ResponseEntity<ApiResponse<?>> add(
            @Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Blueprint created successfully",
                            bp));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, e.getMessage(), null));
        }
    }

    @PutMapping("/{author}/{bpname}/points")
    @Operation(summary = "Add point to blueprint", description = "Updates an existing blueprint by adding a new point")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "202", description = "Point added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blueprint not found", content = @Content)
    })
    public ResponseEntity<ApiResponse<?>> addPoint(
            @Parameter(description = "The name of the author", required = true) @PathVariable String author,
            @Parameter(description = "The name of the blueprint", required = true) @PathVariable String bpname,
            @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(202, "Point added successfully", null));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }
}
