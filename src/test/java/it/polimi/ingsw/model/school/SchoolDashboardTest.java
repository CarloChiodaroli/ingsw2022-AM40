package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TowerColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

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
        TowerColor color = null;
        int oldNumOfTowers;
        for(int j=0;j<2;j++) {
            if(j==0)
                color = TowerColor.WHITE;
            else
                color = TowerColor.BLACK;
            Random random = new Random();
            int i = -(random.nextInt());
            dashboard = new SchoolDashboard(false, color);
            oldNumOfTowers = dashboard.getNumOfTowers();
            assertTrue(dashboard.getTower(i));
            assertTrue(dashboard.getNumOfTowers() >= 0);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
            dashboard = new SchoolDashboard(true, color);
            oldNumOfTowers = dashboard.getNumOfTowers();
            assertTrue(dashboard.getTower(i));
            assertTrue(dashboard.getNumOfTowers() >= 0);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
            dashboard = new SchoolDashboard(true, TowerColor.GREY);
            oldNumOfTowers = dashboard.getNumOfTowers();
            assertTrue(dashboard.getTower(i));
            assertTrue(dashboard.getNumOfTowers() >= 0);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
        }

    }

    @Test
    public void pushTowerTest(){
        SchoolDashboard dashboard;
        TowerColor color = null;
        int oldNumOfTowers;
        for(int j=0;j<2;j++){
            if(j==0)
                color = TowerColor.WHITE;
            else
                color = TowerColor.BLACK;
            Random random = new Random();
            int i = random.nextInt();
            dashboard = new SchoolDashboard(false, color);
            oldNumOfTowers = dashboard.getNumOfTowers();
            dashboard.pushTower(i);
            assertTrue(dashboard.getNumOfTowers()<=8);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
            dashboard = new SchoolDashboard(true, color);
            oldNumOfTowers = dashboard.getNumOfTowers();
            dashboard.pushTower(i);
            assertTrue(dashboard.getNumOfTowers()<=6);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
            dashboard = new SchoolDashboard(true, TowerColor.GREY);
            oldNumOfTowers = dashboard.getNumOfTowers();
            dashboard.pushTower(i);
            assertTrue(dashboard.getNumOfTowers()<=6);
            assertEquals(dashboard.getNumOfTowers(), oldNumOfTowers + i);
        }
    }

}
