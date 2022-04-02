package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TowerColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolDashboardTest {
    @Test
    public void schoolDashboardTest(){
        SchoolDashboard dashboard = new SchoolDashboard(false, TowerColor.WHITE);
        assertEquals(dashboard.getNumOfTowers(), 8);
        assertEquals(dashboard.getTowerColor(), TowerColor.WHITE);

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        assertEquals(dashboard.getNumOfTowers(), 8);
        assertEquals(dashboard.getTowerColor(), TowerColor.BLACK);

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        assertEquals(dashboard.getNumOfTowers(), 6);
        assertEquals(dashboard.getTowerColor(), TowerColor.WHITE);

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        assertEquals(dashboard.getNumOfTowers(), 6);
        assertEquals(dashboard.getTowerColor(), TowerColor.BLACK);

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        assertEquals(dashboard.getNumOfTowers(), 6);
        assertEquals(dashboard.getTowerColor(), TowerColor.GREY);
    }

    @Test
    public void getTowerTest(){
        SchoolDashboard dashboard;
        int oldNumOfTowers;
        dashboard = new SchoolDashboard(false, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(8));
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers - 1);

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(8));
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers - 1);

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers - 1);

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers - 1);

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers - 1);

    }

    @Test
    public void pushTowerTest(){
        SchoolDashboard dashboard;
        int oldNumOfTowers;
        dashboard = new SchoolDashboard(false, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=8);
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers);

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=8);
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers);

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers);

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers);

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers);

    }

}
