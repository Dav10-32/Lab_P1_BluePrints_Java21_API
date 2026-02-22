package edu.eci.arsw.blueprints.persistence.repository;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.BlueprintId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlueprintRepository extends JpaRepository<Blueprint, BlueprintId> {
    List<Blueprint> findByAuthor(String author);
}
