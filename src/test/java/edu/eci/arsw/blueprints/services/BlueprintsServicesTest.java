package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlueprintsServicesTest {

    private BlueprintPersistence persistence;
    private BlueprintsFilter filter;
    private BlueprintsServices services;

    @BeforeEach
    void setUp() {
        persistence = mock(BlueprintPersistence.class);
        filter = mock(BlueprintsFilter.class);
        services = new BlueprintsServices(persistence, filter);
    }

    @Test
    void addNewBlueprintShouldDelegateToPersistence() throws BlueprintPersistenceException {
        Blueprint bp = new Blueprint("author", "name", null);
        services.addNewBlueprint(bp);
        verify(persistence, times(1)).saveBlueprint(bp);
    }

    @Test
    void getBlueprintShouldApplyFilter() throws BlueprintNotFoundException {
        Blueprint bp = new Blueprint("author", "name", null);
        Blueprint filteredBp = new Blueprint("author", "name", null);

        when(persistence.getBlueprint("author", "name")).thenReturn(bp);
        when(filter.apply(bp)).thenReturn(filteredBp);

        Blueprint result = services.getBlueprint("author", "name");

        assertEquals(filteredBp, result);
        verify(filter, times(1)).apply(bp);
    }

    @Test
    void getAllBlueprintsShouldApplyFilterToEach() {
        Blueprint bp1 = new Blueprint("a1", "n1", null);
        Blueprint bp2 = new Blueprint("a2", "n2", null);
        Set<Blueprint> blueprints = new HashSet<>(Arrays.asList(bp1, bp2));

        when(persistence.getAllBlueprints()).thenReturn(blueprints);
        when(filter.apply(any(Blueprint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Blueprint> results = services.getAllBlueprints();

        assertEquals(2, results.size());
        verify(filter, times(2)).apply(any(Blueprint.class));
    }
}
