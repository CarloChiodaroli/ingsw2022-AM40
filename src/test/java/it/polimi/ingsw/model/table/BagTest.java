package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BagTest {
    @Test
    public void bagTest(){
        Bag bag = new Bag(25, 130);
        int i = (int)(Math.random()*5);
        switch (i) {
            case 1:
                assertEquals(bag.getAStudent(), TeacherColor.YELLOW);
            case 2:
                assertEquals(bag.getAStudent(), TeacherColor.PINK);
            case 3:
                assertEquals(bag.getAStudent(), TeacherColor.RED);
            case 4:
                assertEquals(bag.getAStudent(), TeacherColor.GREEN);
            case 5:
                assertEquals(bag.getAStudent(), TeacherColor.BLUE);
        }
    }

}
