package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlueprintsFilterTest {

    @Test
    void redundancyFilterShouldRemoveConsecutiveDuplicates() {
        RedundancyFilter filter = new RedundancyFilter();
        List<Point> points = Arrays.asList(
                new Point(10, 10),
                new Point(10, 10), // Duplicate
                new Point(20, 20),
                new Point(20, 20), // Duplicate
                new Point(10, 10) // Not consecutive duplicate
        );
        Blueprint bp = new Blueprint("john", "house", points);
        Blueprint filtered = filter.apply(bp);

        assertEquals(3, filtered.getPoints().size());
        assertEquals(10, filtered.getPoints().get(0).x());
        assertEquals(20, filtered.getPoints().get(1).x());
        assertEquals(10, filtered.getPoints().get(2).x());
    }

    @Test
    void undersamplingFilterShouldKeepEveryOtherPoint() {
        UndersamplingFilter filter = new UndersamplingFilter();
        List<Point> points = Arrays.asList(
                new Point(0, 0), // Index 0 - Keep
                new Point(1, 1), // Index 1 - Drop
                new Point(2, 2), // Index 2 - Keep
                new Point(3, 3), // Index 3 - Drop
                new Point(4, 4) // Index 4 - Keep
        );
        Blueprint bp = new Blueprint("john", "house", points);
        Blueprint filtered = filter.apply(bp);

        assertEquals(3, filtered.getPoints().size());
        assertEquals(0, filtered.getPoints().get(0).x());
        assertEquals(2, filtered.getPoints().get(1).x());
        assertEquals(4, filtered.getPoints().get(2).x());
    }
}
