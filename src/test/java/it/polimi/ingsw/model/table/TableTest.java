package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
    @Test
    public void tableTest(){
        int studentsYellow = 0;
        int studentsPink = 0;
        int studentsRed = 0;
        int studentsGreen = 0;
        int studentsBlue = 0;
        Table table = new Table(2);
        assertEquals(table.getCloudList().get(0).getId(), "c_1");
        assertEquals(table.getCloudList().get(1).getId(), "c_2");
        for(int i = 0; i < 12; i++){
            assertEquals(table.getIslandList().get(i).getId(), "i_"+(i+1));
        }
        assertEquals(table.getIslandList().size(), 12);
        for(int i = 0; i < 12; i++){
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.YELLOW) == 1)
                studentsYellow++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.PINK) == 1)
                studentsPink++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.RED) == 1)
                studentsRed++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.GREEN) == 1)
                studentsGreen++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.BLUE) == 1)
                studentsBlue++;
        }
        assertEquals(studentsYellow, 2);
        assertEquals(studentsPink, 2);
        assertEquals(studentsRed, 2);
        assertEquals(studentsGreen, 2);
        assertEquals(studentsBlue, 2);
        assertTrue(table.getCoin());
        assertEquals(table.getNumCoin(), 20);
        for(int i = 0; i < 5; i++){
            table.giveCoin();
        }
        assertEquals(table.getNumCoin(), 15);
        for(int i = 0; i < 14; i++){
            table.giveCoin();
        }
        assertFalse(table.getCoin());
        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));
        assertEquals(table.getIslandList().size(), 11);


        studentsYellow = 0;
        studentsPink = 0;
        studentsRed = 0;
        studentsGreen = 0;
        studentsBlue = 0;
        table = new Table(3);
        assertEquals(table.getCloudList().get(0).getId(), "c_1");
        assertEquals(table.getCloudList().get(1).getId(), "c_2");
        assertEquals(table.getCloudList().get(2).getId(), "c_3");
        for(int i = 0; i < 12; i++){
            assertEquals(table.getIslandList().get(i).getId(), "i_"+(i+1));
        }
        assertEquals(table.getIslandList().size(), 12);
        for(int i = 0; i < 12; i++){
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.YELLOW) == 1)
                studentsYellow++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.PINK) == 1)
                studentsPink++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.RED) == 1)
                studentsRed++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.GREEN) == 1)
                studentsGreen++;
            if(table.getIslandList().get(i).howManyStudents(TeacherColor.BLUE) == 1)
                studentsBlue++;
        }
        assertEquals(studentsYellow, 2);
        assertEquals(studentsPink, 2);
        assertEquals(studentsRed, 2);
        assertEquals(studentsGreen, 2);
        assertEquals(studentsBlue, 2);
        assertTrue(table.getCoin());
        assertEquals(table.getNumCoin(), 20);
        for(int i = 0; i < 5; i++){
            table.giveCoin();
        }
        assertEquals(table.getNumCoin(), 15);
        for(int i = 0; i < 14; i++){
            table.giveCoin();
        }
        assertFalse(table.getCoin());
        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));
        assertEquals(table.getIslandList().size(), 11);


    }


}
