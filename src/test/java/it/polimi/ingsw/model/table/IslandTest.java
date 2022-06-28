package it.polimi.ingsw.model.table;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.table.Island;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link Island} methods
 */
public class IslandTest {
    Island to = new Island("C_2");

    /**
     * Test correct merge islands
     */
    @Test
    public void elaborationMergeIslandIdTest() {
        Island is1 = new Island("I_1_2");
        Island is2 = new Island("I_3_4");
        assertEquals("I_1_2", is1.getId());
        assertEquals("I_3_4", is2.getId());
        assertEquals("I_1_2_3_4", new Island(is1, is2).getId());
        assertEquals(2, new Island(is1, is2).howManyEquivalents());
    }

    /**
     * Test correct movement of students in islands
     */
    @Test
    public void moveStudentTest() {
        Island island = new Island("I_1");
        Island newisland = new Island("I_2");
        for (TeacherColor tc : TeacherColor.values()) {
            for (int i = 0; i < 5; i++) {
                island.addStudent(tc);
            }
        }
        assertEquals(25, island.howManyTotStudents());
        for (TeacherColor tc : TeacherColor.values()) {
            assertEquals(5, island.howManyStudents(tc));
        }

        newisland.addStudentFromSmallIsland(island);
        assertEquals(25, island.howManyTotStudents());
        for (TeacherColor tc : TeacherColor.values()) {
            assertEquals(5, island.howManyStudents(tc));
        }


    }

    /**
     * Test prohibition cards
     */
    @Test
    public void noEntryTest() {
        Island is1, is2;
        is1 = new Island("I_1");
        is2 = new Island("I_2");

        is1.setNoEntry(true);
        is2.setNoEntry(true);
        assertEquals(true, new Island(is1, is2).hasNoEntryTile());

        is1.setNoEntry(true);
        is2.removeNoEntryTile();
        assertEquals(false, new Island(is1, is2).hasNoEntryTile());

        is1.setNoEntry(false);
        is2.setNoEntry(true);
        assertEquals(false, new Island(is1, is2).hasNoEntryTile());

        is1.setNoEntry(false);
        is2.setNoEntry(false);
        assertEquals(false, new Island(is1, is2).hasNoEntryTile());

        assertFalse(is1.hasNoEntryTile());
    }

    /**
     * Test tower presence and color
     */
    @Test
    public void towersTest(){
        Island is1;
        is1 = new Island("I_1");

        assertEquals(0, is1.howManyTowers());
        is1.setInfluence(TowerColor.WHITE);

        assertEquals(Optional.of(TowerColor.WHITE) , is1.getTowerColor());
    }

    /**
     * Test merge 2 or 3 islands
     */
    @Test
    public void mergeTest() {
        // 2 Island
        Island island1 = new Island("I_1");
        island1.setTower(true);
        Island island2 = new Island("I_2");
        Island newIsland = new Island(island1, island2);
        assertEquals("I_1_2", newIsland.getId());
        assertEquals(2, newIsland.howManyEquivalents());
        assertFalse(newIsland.hasNoEntryTile());
        assertTrue(newIsland.hasTowers());
        // 3 Island,...
        Island island3 = new Island("I_3");
        assertEquals(1, island3.howManyEquivalents());
        assertEquals(2, newIsland.howManyEquivalents());
        Island last = new Island(newIsland, island3);
        assertEquals("I_1_2_3", last.getId());
        assertEquals(3, last.howManyEquivalents());
        assertFalse(last.hasNoEntryTile());
        assertTrue(last.hasTowers());
    }


}