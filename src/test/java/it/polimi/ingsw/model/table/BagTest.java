package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class BagTest {
    @Test
    public void bagTest(){
        Bag bag = new Bag(26, 130);
        assertEquals(bag.getAStudent(), TeacherColor.YELLOW);
        assertEquals(bag.getAStudent(), TeacherColor.PINK);
        assertEquals(bag.getAStudent(), TeacherColor.RED);
        assertEquals(bag.getAStudent(), TeacherColor.GREEN);
        assertEquals(bag.getAStudent(), TeacherColor.BLUE);

    }

}
