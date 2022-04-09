package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
    @Test
    public void generateCloudsTest(){
        Table table = new Table(2);
        assertEquals("c_1",table.getCloudList().get(0).getId());
        assertEquals("c_2", table.getCloudList().get(1).getId() );

        table = new Table(3);
        assertEquals( "c_1", table.getCloudList().get(0).getId());
        assertEquals("c_2", table.getCloudList().get(1).getId());
        assertEquals( "c_3", table.getCloudList().get(2).getId());

    }

    @Test
    public void generateIslandTest(){
        Table table = new Table(2);
        for(int i = 0; i < 12; i++){
            assertEquals("i_"+(i+1),table.getIslandList().get(i).getId());
        }
        assertEquals(12, table.getIslandList().size());

        table = new Table(3);

        for(int i = 0; i < 12; i++){
            assertEquals("i_"+(i+1), table.getIslandList().get(i).getId() );
        }
        assertEquals(12, table.getIslandList().size());
    }

    @Test
    public void fillIslandTest(){
        int studentsYellow = 0;
        int studentsPink = 0;
        int studentsRed = 0;
        int studentsGreen = 0;
        int studentsBlue = 0;
        int emptyIsland = 0;
        int emptyColor = 0;
        int  indexEmpty = 0;
        boolean findIndexEmpty = false;
        int indexEmpty2 = 0;
        Table table;
        for(int j = 2; j <=3; j++) {
            table = new Table(j);
            for (int i = 0; i < 12; i++) {
                if (table.getIslandList().get(i).howManyStudents(TeacherColor.YELLOW) == 1)
                    studentsYellow++;
                if (table.getIslandList().get(i).howManyStudents(TeacherColor.PINK) == 1)
                    studentsPink++;
                if (table.getIslandList().get(i).howManyStudents(TeacherColor.RED) == 1)
                    studentsRed++;
                if (table.getIslandList().get(i).howManyStudents(TeacherColor.GREEN) == 1)
                    studentsGreen++;
                if (table.getIslandList().get(i).howManyStudents(TeacherColor.BLUE) == 1)
                    studentsBlue++;
                for (TeacherColor color : TeacherColor.values()) {
                    if (table.getIslandList().get(i).howManyStudents(color) == 0)
                        emptyColor++;
                }
                if (emptyColor == 5) {
                    emptyIsland++;
                    if (!findIndexEmpty) {
                        indexEmpty = i;
                        findIndexEmpty = true;
                    } else
                        indexEmpty2 = i;
                }
                emptyColor = 0;
            }
            assertEquals(2, studentsYellow);
            assertEquals(2, studentsPink);
            assertEquals(2, studentsRed);
            assertEquals(2, studentsGreen);
            assertEquals(2, studentsBlue);
            assertEquals(2, emptyIsland);
            assertEquals(indexEmpty, indexEmpty2 - 6);
            studentsYellow = 0;
            studentsPink = 0;
            studentsRed = 0;
            studentsGreen = 0;
            studentsBlue = 0;
            emptyIsland = 0;
        }
    }

    @Test
    public void coinTest(){
        Table table;
        for(int j = 2; j <= 3; j++) {
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

    @Disabled("<fixing time>")
    @Test
    public void mergeIslandTest(){
        Table table = new Table(2);

        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));
        assertEquals(11, table.getIslandList().size());


        table = new Table(3);

        table.mergeIsland(table.getIslandList().get(2), table.getIslandList().get(3));
        assertEquals(11, table.getIslandList().size());


    }


}
