package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link Table} methods
 */
public class TableTest {
    /**
     * Test generate clouds correctly
     */
    @Test
    public void generateCloudsTest() {
        Table table = new Table(2);
        table.buildClouds();
        assertEquals("c_1", table.getCloudList().get(0).getId());
        assertEquals("c_2", table.getCloudList().get(1).getId());

        assertEquals((StudentsManager) table.getCloudList().get(0), table.getCloudById("c_1").get());
        assertEquals((StudentsManager) table.getCloudList().get(1), table.getCloudById("c_2").get());

        table = new Table(3);
        table.buildClouds();
        assertEquals("c_1", table.getCloudList().get(0).getId());
        assertEquals("c_2", table.getCloudList().get(1).getId());
        assertEquals("c_3", table.getCloudList().get(2).getId());

        assertEquals((StudentsManager) table.getCloudList().get(0), table.getCloudById("c_1").get());
        assertEquals((StudentsManager) table.getCloudList().get(1), table.getCloudById("c_2").get());
        assertEquals((StudentsManager) table.getCloudList().get(2), table.getCloudById("c_3").get());
        Table finalTable = table;
        assertThrows(NoSuchElementException.class , () -> finalTable.getCloudById("c_4").orElseThrow());
    }

    /**
     * Test generate islands correctly
     */
    @Test
    public void generateIslandTest() {
        Table table = new Table(2);
        for (int i = 0; i < 12; i++) {
            assertEquals("i_" + (i + 1), table.getIslandList().get(i).getId());
        }
        assertEquals(12, table.getIslandList().size());

        table = new Table(3);

        for (int i = 0; i < 12; i++) {
            assertEquals("i_" + (i + 1), table.getIslandList().get(i).getId());
        }
        assertEquals(12, table.getIslandList().size());
    }

    /**
     * Test correctly islands fill with 2 players
     */
    @Test
    public void fillIsland2playerTest() {
        int studentsYellow = 0;
        int studentsPink = 0;
        int studentsRed = 0;
        int studentsGreen = 0;
        int studentsBlue = 0;
        int emptyIsland = 0;
        int emptyColor = 0;
        int indexEmpty = 0;
        boolean findIndexEmpty = false;
        int indexEmpty2 = 0;
        Table table;
        table = new Table(2);
        for (int i = 0; i < 12; i++) {
            boolean emptyCandidate = true;
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.YELLOW) == 1) {
                studentsYellow++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.PINK) == 1) {
                studentsPink++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.RED) == 1) {
                studentsRed++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.GREEN) == 1) {
                studentsGreen++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.BLUE) == 1) {
                studentsBlue++;
                emptyCandidate = false;
            }

                /*for (TeacherColor color : TeacherColor.values()) {
                    if (table.getIslandList().get(i).howManyStudents(color) == 0)
                        emptyColor++;
                }*/
            if (emptyCandidate) {
                emptyIsland++;
                if (!findIndexEmpty) {
                    indexEmpty = i;
                    findIndexEmpty = true;
                } else {
                    indexEmpty2 = i;
                }
            }
                /*if (emptyColor == 5) {
                    emptyIsland++;
                    if (!findIndexEmpty) {
                        indexEmpty = i;
                        findIndexEmpty = true;
                    } else
                        indexEmpty2 = i;
                }*/
            //emptyColor = 0;
        }
        assertEquals(2, studentsYellow);
        assertEquals(2, studentsPink);
        assertEquals(2, studentsRed);
        assertEquals(2, studentsGreen);
        assertEquals(2, studentsBlue);
        assertEquals(2, emptyIsland);
        assertEquals(indexEmpty, indexEmpty2 - 6);
    }

    /**
     * Test correctly islands fill with 3 players
     */
    @Test
    public void fillIsland3playerTest() {
        int studentsYellow = 0;
        int studentsPink = 0;
        int studentsRed = 0;
        int studentsGreen = 0;
        int studentsBlue = 0;
        int emptyIsland = 0;
        int emptyColor = 0;
        int indexEmpty = 0;
        boolean findIndexEmpty = false;
        int indexEmpty2 = 0;
        Table table;
        table = new Table(3);
        for (int i = 0; i < 12; i++) {
            boolean emptyCandidate = true;
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.YELLOW) == 1) {
                studentsYellow++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.PINK) == 1) {
                studentsPink++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.RED) == 1) {
                studentsRed++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.GREEN) == 1) {
                studentsGreen++;
                emptyCandidate = false;
            }
            if (table.getIslandList().get(i).howManyStudents(TeacherColor.BLUE) == 1) {
                studentsBlue++;
                emptyCandidate = false;
            }

                /*for (TeacherColor color : TeacherColor.values()) {
                    if (table.getIslandList().get(i).howManyStudents(color) == 0)
                        emptyColor++;
                }*/
            if (emptyCandidate) {
                emptyIsland++;
                if (!findIndexEmpty) {
                    indexEmpty = i;
                    findIndexEmpty = true;
                } else {
                    indexEmpty2 = i;
                }
            }
                /*if (emptyColor == 5) {
                    emptyIsland++;
                    if (!findIndexEmpty) {
                        indexEmpty = i;
                        findIndexEmpty = true;
                    } else
                        indexEmpty2 = i;
                }*/
            //emptyColor = 0;
        }
        assertEquals(2, studentsYellow);
        assertEquals(2, studentsPink);
        assertEquals(2, studentsRed);
        assertEquals(2, studentsGreen);
        assertEquals(2, studentsBlue);
        assertEquals(2, emptyIsland);
        assertEquals(indexEmpty, indexEmpty2 - 6);
    }

    /**
     * Test remove and add coins
     */
    @Test
    public void coinTest() {
        Table table;
        for (int j = 2; j <= 3; j++) {
            table = new Table(j);
            assertTrue(table.getCoin());
            assertEquals(20, table.getNumCoin());
            for (int i = 0; i < 5; i++) {
                table.giveCoin();
            }
            assertEquals(15, table.getNumCoin());
            for (int i = 0; i < 15; i++) {
                table.giveCoin();
            }
            assertFalse(table.getCoin());
        }
    }

    /**
     * Test merge islands
     */
    @Test
    public void mergeIslandTest() {
        Table table = new Table(2);
        List<Island> prevIslands = new ArrayList<>(table.getIslandList());

        assertEquals(12, prevIslands.size());

        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));

        List<Island> afterIslands = table.getIslandList();

        assertEquals(11, table.getIslandList().size());

        assertEquals(prevIslands.get(0), afterIslands.get(0));
        assertEquals(prevIslands.get(1), afterIslands.get(1));
        assertNotEquals(prevIslands.get(2), afterIslands.get(2));
        assertNotEquals(prevIslands.get(3), afterIslands.get(2));
        assertEquals(prevIslands.get(4), afterIslands.get(3));
        assertEquals(prevIslands.get(5), afterIslands.get(4));
        assertEquals(prevIslands.get(6), afterIslands.get(5));
        assertEquals(prevIslands.get(7), afterIslands.get(6));
        assertEquals(prevIslands.get(8), afterIslands.get(7));
        assertEquals(prevIslands.get(9), afterIslands.get(8));
        assertEquals(prevIslands.get(10), afterIslands.get(9));
        assertEquals(prevIslands.get(11), afterIslands.get(10));

        table = new Table(3);

        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));
        assertEquals(table.getIslandList().size(), 11);
    }



}
