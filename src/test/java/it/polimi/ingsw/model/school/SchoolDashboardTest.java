package it.polimi.ingsw.model.school;

import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.player.school.SchoolDashboard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolDashboardTest {
    @Test
    public void schoolDashboardTest(){
        SchoolDashboard dashboard = new SchoolDashboard(false, TowerColor.WHITE);
        assertEquals(8, dashboard.getNumOfTowers());
        assertEquals(TowerColor.WHITE, dashboard.getTowerColor());

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        assertEquals(8, dashboard.getNumOfTowers());
        assertEquals(TowerColor.BLACK, dashboard.getTowerColor());

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        assertEquals(6, dashboard.getNumOfTowers());
        assertEquals(TowerColor.WHITE, dashboard.getTowerColor());

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        assertEquals(6, dashboard.getNumOfTowers());
        assertEquals(TowerColor.BLACK, dashboard.getTowerColor());

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        assertEquals(6, dashboard.getNumOfTowers());
        assertEquals(TowerColor.GREY, dashboard.getTowerColor());
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
        assertEquals(oldNumOfTowers - 1, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(8));
        assertEquals(oldNumOfTowers - 1, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(oldNumOfTowers - 1, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(oldNumOfTowers - 1, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertTrue(dashboard.getTower(1));
        assertTrue(dashboard.getNumOfTowers() >= 0);
        assertFalse(dashboard.getTower(6));
        assertEquals(oldNumOfTowers - 1, dashboard.getNumOfTowers());

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
        assertEquals(oldNumOfTowers, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(false, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=8);
        assertEquals(oldNumOfTowers, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.WHITE);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(oldNumOfTowers, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.BLACK);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(oldNumOfTowers, dashboard.getNumOfTowers());

        dashboard = new SchoolDashboard(true, TowerColor.GREY);
        oldNumOfTowers = dashboard.getNumOfTowers();
        assertFalse(dashboard.pushTower(1));
        dashboard.getTower(3);
        assertFalse(dashboard.pushTower(4));
        assertTrue(dashboard.pushTower(3));
        assertTrue(dashboard.getNumOfTowers()<=6);
        assertEquals(oldNumOfTowers, dashboard.getNumOfTowers());

    }

}