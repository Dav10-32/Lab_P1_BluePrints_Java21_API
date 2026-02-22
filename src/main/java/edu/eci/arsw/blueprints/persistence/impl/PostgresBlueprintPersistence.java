package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.BlueprintId;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.repository.BlueprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
@Primary
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    private final BlueprintRepository blueprintRepository;

    @Autowired
    public PostgresBlueprintPersistence(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprintRepository.existsById(new BlueprintId(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("Blueprint already exists: " + bp.getAuthor() + "/" + bp.getName());
        }
        blueprintRepository.save(bp);
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return blueprintRepository.findById(new BlueprintId(author, name))
                .orElseThrow(() -> new BlueprintNotFoundException("Blueprint not found: " + author + "/" + name));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> blueprints = new HashSet<>(blueprintRepository.findByAuthor(author));
        if (blueprints.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints found for author: " + author);
        }
        return blueprints;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(blueprintRepository.findAll());
    }

    @Override
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        Blueprint bp = getBlueprint(author, name);
        bp.addPoint(new Point(x, y));
        blueprintRepository.save(bp);
    }
}
