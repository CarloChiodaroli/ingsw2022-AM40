package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
    Island to = new Island("C_2");

    @Test
    public void elaborationMergeIslandIdTest() {
        Island is1 = new Island("I_1_2");
        Island is2 = new Island("I_3_4");
        assertEquals("I_1_2", is1.getId());
        assertEquals("I_3_4", is2.getId());
        assertEquals("I_1_2_3_4", new Island(is1, is2).getId());
        assertEquals(2, new Island(is1, is2).howManyEquivalents());
    }

    @Test
    public void MoveStudentTest() {
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

        newisland.addStudentfromSmallIsland(island);
        assertEquals(25, island.howManyTotStudents());
        for (TeacherColor tc : TeacherColor.values()) {
            assertEquals(5, island.howManyStudents(tc));
        }


    }

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

    @Test
    public void towersTest(){
        Island is1;
        is1 = new Island("I_1");

        assertEquals(0, is1.howManyTowers());
        is1.setInfluence(TowerColor.WHITE);

        assertEquals(Optional.of(TowerColor.WHITE) , is1.getTowerColor());
    }

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